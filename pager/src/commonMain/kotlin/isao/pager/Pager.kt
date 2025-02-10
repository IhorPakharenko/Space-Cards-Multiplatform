package isao.pager

import arrow.core.Either
import arrow.core.getOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

//TODO sort pages by key to support loading pages out of order
//TODO check why pager produces more updates than we expect when observing db
class Pager<Key, Failure, Value>(
  val config: Config<Key>,
  val observeLocal: (Key, Config<Key>) -> Flow<Either<Failure, List<Value>>>,
  //TODO consider making something like FetchStrategy that would combine LoadOrder and fetch
  // to avoid having to pass a no-op fetch in the case of LoadOrder.LocalOnly
  val fetch: suspend (Key, Config<Key>) -> Either<Failure, RemoteSuccess>,
  val nextKey: Flow<Key>,
  val shouldRetry: Flow<Key>,
  //TODO limit the amount of observed pages (e.g. only observe currently visible pages)
  val shouldObserve: Flow<List<Key>> = emptyFlow(),
) {
  private val pages = MutableStateFlow(listOf(createPage(config.initialRequestKey)))

  private val localJobs = mutableMapOf<Key, Job>()
  private val localJobMutex = Mutex()
  private val remoteJobs = mutableMapOf<Key, Job>()
  private val remoteJobMutex = Mutex()

  val flow = channelFlow {
    val channelContext = coroutineContext

    // Add more pages
    launch {
      combine(
        nextKey.distinctUntilChanged(),
        pages.map { pages -> pages.any { it.isLoading || it.isLast } }.distinctUntilChanged(),
      ) { nextKey, isAnyPageLoadingOrLast ->
        pages.update { pages ->
          //TODO right now, we can only add new pages when all current pages are not loading,
          // otherwise we might load pages after the end of pagination.
          // This disables loading multiple pages at once, so it should be fixed.
          if (isAnyPageLoadingOrLast) {
            return@update pages
          }

          return@update pages.toMutableList().apply {
            if (none { it.key == nextKey }) {
              add(createPage(nextKey))
            }
          }
        }
      }.collect()
    }

    // Load pages from local storage
    launch {
      pages
        .map { pages ->
          pages.filter { it.isFirstLocalLoading }
        }.distinctUntilChanged()
        .collectLatest { pagesToLoad ->
          localJobMutex.withLock {
            pagesToLoad.forEach { page ->
              localJobs.getOrPut(page.key) {
                launch(channelContext) {
                  observePage(page.key).collect()
                }
              }
            }
          }
        }
    }

    // Load pages from remote
    launch {
      pages
        .map { pages ->
          pages.filter { it.isRemoteLoading }
        }.distinctUntilChanged()
        .collectLatest { pagesToLoad ->
          remoteJobMutex.withLock {
            pagesToLoad.forEach { page ->
              remoteJobs.getOrPut(page.key) {
                launch(channelContext) {
                  try {
                    fetchPage(page.key)
                  } finally {
                    remoteJobs.remove(page.key)
                  }
                }
              }
            }
          }
        }
    }

    // Retry pages
    launch {
      shouldRetry.collect { key ->
        retry(key)
      }
    }

    // Emit pages
    launch {
      pages.collect {
        send(it)
      }
    }
    //TODO observe and fetch already use Dispatchers.IO, and we don't do
    // any demanding work aside from them. Should we remove Dispatchers.IO here?
  }.flowOn(Dispatchers.IO)

  private fun createPage(key: Key): Page<Key, Failure, Value> = Page(
    key = key,
    lastValidItems = emptyList(),
    localFailure = null,
    remoteFailure = null,
    isLast = false,
    isFirstLocalLoading = when (config.loadOrder) {
      LoadOrder.LocalThenIfInvalidRemote -> true
      LoadOrder.LocalOnly -> true
      LoadOrder.BothSimultaneously -> true
    },
    isRemoteLoading = when (config.loadOrder) {
      LoadOrder.LocalThenIfInvalidRemote -> false
      LoadOrder.LocalOnly -> false
      LoadOrder.BothSimultaneously -> true
    },
  )

  //TODO this method assumes we only want to retry remote
  private fun retry(key: Key) {
    pages.update { pages ->
      pages.updateFirstOrNull({ it.key == key }) { it.copy(isRemoteLoading = true) } ?: pages
    }
  }

  //TODO Running a flow exclusively for its side effect does not appear to be common.
  // I feel like this approach is justified here, but are there risks?
  private fun observePage(key: Key) =
    observeLocal(key, config).distinctUntilChanged().map { localResult ->
      pages.update { pages ->
        pages.updateFirstOrNull({ it.key == key }) { page ->
          page
            .copy(
              lastValidItems = localResult.getOrNull() ?: page.lastValidItems,
              isFirstLocalLoading = false,
              localFailure = localResult.leftOrNull(),
              isRemoteLoading = when (config.loadOrder) {
                LoadOrder.LocalThenIfInvalidRemote -> {
                  val resultItemCount = localResult.map { it.size }.getOrElse { 0 }
                  resultItemCount < config.pageSize
                }

                LoadOrder.LocalOnly, LoadOrder.BothSimultaneously -> page.isRemoteLoading
              },
              isLast = when (config.loadOrder) {
                LoadOrder.LocalOnly -> {
                  val resultItemCount = localResult.map { it.size }.getOrElse { 0 }
                  resultItemCount < config.pageSize
                }

                LoadOrder.LocalThenIfInvalidRemote, LoadOrder.BothSimultaneously -> page.isLast
              },
            )
        } ?: pages
      }
    }

  private suspend fun fetchPage(key: Key) {
    val remoteResult = fetch(key, config)
    val localResult = if (remoteResult.isRight()) observeLocal(key, config).first() else null

    //TODO make the commented check readable

//      check(page.isLast || resultItemCount == config.pageSize) {
//        "Page $key wasn't marked last, but haven't received " +
//          "enough items after the first fetch. " +
//          "Make sure updateLocal reports the end of paging if not enough items are loaded"
//      }
    pages.update { pages ->
      pages.updateFirstOrNull({ it.key == key }) { page ->
        page.copy(
          lastValidItems = localResult?.getOrNull() ?: page.lastValidItems,
          localFailure = localResult?.leftOrNull(),
          remoteFailure = remoteResult.leftOrNull(),
          isLast = remoteResult.getOrNull()?.isLastPage ?: page.isLast,
          isRemoteLoading = false,
        )
      } ?: pages
    }
  }
}

data class Page<Key, Failure, Value>(
  val key: Key,
  val lastValidItems: List<Value>,
  val isFirstLocalLoading: Boolean,
  val localFailure: Failure?,
  val isRemoteLoading: Boolean,
  val remoteFailure: Failure?,
  val isLast: Boolean,
) {
  val isLoading
    get() = isFirstLocalLoading || isRemoteLoading
}

data class RemoteSuccess(val isLastPage: Boolean)

data class Config<Key>(
  val loadOrder: LoadOrder,
  val pageSize: Int,
  val initialRequestKey: Key,
)

enum class LoadOrder {
  LocalThenIfInvalidRemote,
  BothSimultaneously,
  LocalOnly,
}
