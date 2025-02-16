package com.isao.spacecards.feature.feed

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.PageAstrobinImagesUseCase
import com.isao.spacecards.feature.designsystem.MviViewModel
import com.isao.spacecards.feature.designsystem.Reducer
import isao.pager.Config
import isao.pager.LoadOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.microseconds

// Declare ViewModel in Koin Module until this issue is fixed:
// https://github.com/InsertKoinIO/koin-annotations/issues/185
// @KoinViewModel
//TODO handle process death
class FeedViewModel(
  private val astrobinImageRepository: AstrobinImageRepository,
  private val pageAstrobinImagesUseCase: PageAstrobinImagesUseCase,
) : MviViewModel<FeedUiState, Nothing, FeedIntent>(
    FeedUiState(),
  ) {
  init {
    observeContinuousChanges(
      dependingOnState = { it.startFromInstant },
    ) { getAstrobinItems(it) }
  }

  override fun mapIntents(intent: FeedIntent): Flow<Reducer<FeedUiState>> = when (intent) {
    is FeedIntent.StartFromMillisUTC -> flowOfUpdateState {
      // Query images shot earlier than the last moment of the day
      copy(
        startFromInstant = Instant
          .fromEpochMilliseconds(
            intent.millis,
          ).plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
          .minus(1.microseconds)
          .toLocalDateTime(TimeZone.UTC)
          .toInstant(TimeZone.currentSystemDefault()),
      )
    }

    is FeedIntent.Like -> {
      likeItem(intent.item)
    }

    is FeedIntent.Dislike -> {
      dislikeItem(intent.item)
    }

    FeedIntent.Retry -> flow {
      uiStateSnapshot.value.items.filter { it.remoteFailure != null }.forEach {
        retryFlow.emit(it.page)
      }
    }
  }

  private val retryFlow = MutableSharedFlow<Instant?>()

  private fun getAstrobinItems(startFromInstant: Instant?): Flow<Reducer<FeedUiState>> =
    pageAstrobinImagesUseCase(
      config = Config(
        loadOrder = LoadOrder.BothSimultaneously,
        pageSize = 20,
        initialRequestKey = startFromInstant,
      ),
      shouldRetry = retryFlow,
      currentPage = uiStateSnapshot
        .map { state ->
          state.items
            .firstOrNull { it.lastValidData != null && it.lastValidData.viewedAt == null }
            ?.page
        }.distinctUntilChanged(),
    ).map { pages ->
      updateState {
        copy(
          items = pages
            .flatMap { page ->
              if (page.lastValidItems.isEmpty()) {
                listOf(
                  PagedItem(
                    page = page.key,
                    lastValidData = null,
                    isLoading = page.isLoading,
                    remoteFailure = page.remoteFailure,
                  ),
                )
              } else {
                page.lastValidItems.map {
                  PagedItem(
                    page = page.key,
                    lastValidData = it,
                    isLoading = page.isLoading,
                    remoteFailure = page.remoteFailure,
                  )
                }
              }
            },
        )
      }
    }

  private fun likeItem(item: PagedItem): Flow<Reducer<FeedUiState>> = flow {
    astrobinImageRepository.setBookmarkedAndViewed(item.lastValidData!!.id, at = Clock.System.now())
  }

  private fun dislikeItem(item: PagedItem): Flow<Reducer<FeedUiState>> = flow {
    astrobinImageRepository.setViewed(item.lastValidData!!.id, at = Clock.System.now())
  }
}
