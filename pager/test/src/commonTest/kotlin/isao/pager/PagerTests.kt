package isao.pager

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

//TODO fix and update PagerTests

//TODO it's impossible to run an individual test in modules targeting Android:
// https://github.com/kotest/kotest/issues/3651
class PagerTests : FunSpec() {
  //TODO configure in project config and intellij properties:
  // https://kotest.io/docs/framework/project-config.html
  // https://kotest.io/docs/intellij/intellij-properties.html
  override fun isolationMode() = IsolationMode.InstancePerLeaf

  //TODO add test when only one of two local items are present. This should trigger remote load
  init { //TODO think of more descriptive names for full local, non-full local, etc.
    context("Given full local") {
      //TODO this must fail because local6 is missing and remote is not implemented here
      val local = mapOf(
        0 to flowOf(listOf("local1", "local2").right()),
        1 to flowOf(listOf("local3", "local4").right()),
        2 to flowOf(listOf("local5", "local6").right()),
      )
      context("When collected") {
        val subject = getSubject(
          localData = local,
          remoteData = emptyMap(),
          updateLocal = { _, _ -> throw AssertionError("UpdateLocal should not be called") },
        )
        test("Then produce data page by page, updating loading states") {
          subject.flow.test {
            verifyAllStatesForLocalOnly()
          }
        }

        test("Then end of pagination should be reached after loading all pages") {
          subject.flow.test {
            val events = cancelAndConsumeRemainingEvents().filter { !it.isTerminal }
            val statesBeforeLast = events.dropLast(1).map { (it as Event.Item).value }
            statesBeforeLast.forEach { pages ->
              pages.forAll { page ->
                page.isLast shouldBe false
              }
            }
            val lastState = (events.last() as Event.Item).value
            lastState.dropLast(1).forEach { page ->
              page.isLast shouldBe false
            }
            lastState.last().isLast shouldBe true
          }
        }
      }
    }

    context("Given non-full local and non-empty remote") {
      val local = mapOf(
        0 to flowOf(emptyList<String>().right()),
        1 to flowOf(listOf("local3").right()), // One item is not enough, should try to load remote
        2 to flowOf(emptyList<String>().right()),
      )
      val remote = mapOf(
        0 to listOf("remote1", "remote2").right(),
        1 to listOf("remote3", "remote4").right(),
        2 to listOf("remote5", "remote6").right(),
      )
      context("When collected") {
        val subject = getSubject(localData = local, remoteData = remote)
        test("Then final state contains all items from remote") {
          subject.flow.test {
            expectMostRecentItem() shouldContainExactly listOf(
              Page(
                key = 0,
                items = listOf("remote1", "remote2"),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.NotLoading,
              ),
              Page(
                key = 1,
                items = listOf("remote3", "remote4"),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.NotLoading,
              ),
              Page(
                key = 2,
                items = listOf("remote5", "remote6"),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.NotLoading,
                isLast = true,
              ),
            )
          }
        }
      }
    }

    context("Given empty local and remote producing errors") {
      val local = mapOf(
        0 to flowOf(emptyList<String>().right()),
        1 to flowOf(emptyList<String>().right()),
        2 to flowOf(emptyList<String>().right()),
      )
      val remote = mutableMapOf(
        0 to listOf("remote1", "remote2").right(),
        1 to "error".left(),
        2 to "error2".left(),
      )
      context("When collected") {
        val shouldRetry = MutableSharedFlow<Int>()
        val subject = getSubject(localData = local, remoteData = remote, shouldRetry = shouldRetry)
        test("Then final state contains remote data before error and all errors") {
          subject.flow.test {
            expectMostRecentItem() shouldContainExactly listOf(
              Page(
                key = 0,
                items = listOf("remote1", "remote2"),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.NotLoading,
              ),
              Page(
                key = 1,
                items = emptyList(),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.Fail("error"),
              ),
              Page(
                key = 2,
                items = emptyList(),
                local = DataSourceState.NotLoading,
                remote = DataSourceState.Fail("error2"),
              ),
            )
          }
        }

        context("And retry called on first error") {
          subject.flow.test {
            expectMostRecentItem()

            remote[1] = listOf("remote3", "remote4").right()
            shouldRetry.emit(1)

            test("Then final state contains remote data before second error and second error") {
              expectMostRecentItem() shouldContainExactly listOf(
                Page(
                  key = 0,
                  items = listOf("remote1", "remote2"),
                  local = DataSourceState.NotLoading,
                  remote = DataSourceState.NotLoading,
                ),
                Page(
                  key = 1,
                  items = listOf("remote3", "remote4"),
                  local = DataSourceState.NotLoading,
                  remote = DataSourceState.NotLoading,
                ),
                Page(
                  key = 2,
                  items = emptyList(),
                  local = DataSourceState.NotLoading,
                  remote = DataSourceState.Fail("error2"),
                ),
              )
            }
          }
        }
      }
    }
  }
}

private suspend fun getSubject(
  localData: Map<Int, Flow<Either<String, List<String>>>>,
  remoteData: Map<Int, Either<String, List<String>>>,
  config: Config<Int> = Config(
    pageSize = 2,
    initialRequestKey = 0,
  ),
  getLocal: ((Int?, Config<Int>) -> Flow<Either<String, List<String>>>)? = null,
  updateLocal: (suspend (Int?, Config<Int>) -> Either<String, RemoteSuccess>)? = null,
  getNextKey: (Page<Int, String>) -> Int? = { page ->
    (page.key!! + 1).takeIf { it < localData.size }
  },
//  nextKey: Flow<Int?> = flow {  }
  shouldRetry: Flow<Int> = emptyFlow(),
): Pager<Int, String, String, String> {
  // Emulate the ability of updateLocal to update local data
  val mutableLocalData = localData.mapValues { (_, flow) ->
    MutableStateFlow<Either<String, List<String>>>(Either.Right(emptyList())).also {
      flow.collect { items -> it.value = items }
    }
  }
  return Pager<Int, String, String, String>(
    config = config,
    observeLocal = getLocal ?: { key, _ -> mutableLocalData[key]!! },
    fetch = updateLocal ?: updateLocal@{ key, _ ->
      // If remote data contains an error, return it and don't update local data
      remoteData[key]!!.leftOrNull()?.let { return@updateLocal it.left() }
      // Update local data with remote data
      mutableLocalData[key]!!.value = remoteData[key]!!
      // Report success
      return@updateLocal RemoteSuccess(isLast = key == remoteData.size - 1, total = null).right()
    },
    nextKey = flow { mutableLocalData.forEach { emit(it.key) } },
    shouldRetry = shouldRetry,
  )
}

private suspend fun ReceiveTurbine<List<Page<Int, String>>>.verifyAllStatesForLocalOnly() {
  awaitItem() shouldContainExactly emptyList()
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = null,
      local = DataSourceState.Loading,
      remote = DataSourceState.NotLoading,
    ),
  )
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
  )
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 1,
      items = null,
      local = DataSourceState.Loading,
      remote = DataSourceState.NotLoading,
    ),
  )
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 1,
      items = listOf("local3", "local4"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
  )
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 1,
      items = listOf("local3", "local4"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 2,
      items = null,
      local = DataSourceState.Loading,
      remote = DataSourceState.NotLoading,
    ),
  )
  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 1,
      items = listOf("local3", "local4"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 2,
      items = listOf("local5", "local6"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
  )

  awaitItem() shouldContainExactly listOf(
    Page(
      key = 0,
      items = listOf("local1", "local2"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 1,
      items = listOf("local3", "local4"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
    Page(
      key = 2,
      items = listOf("local5", "local6"),
      local = DataSourceState.NotLoading,
      remote = DataSourceState.NotLoading,
    ),
  )
}
