package com.isao.spacecards.feature.feed

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.PageAstrobinImagesUseCase
import com.isao.spacecards.feature.designsystem.MviViewModel
import isao.pager.Config
import isao.pager.LoadOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.microseconds

internal typealias FeedPartialState = (FeedUiState) -> FeedUiState

// Declare ViewModel in Koin Module until this issue is fixed:
// https://github.com/InsertKoinIO/koin-annotations/issues/185
// @KoinViewModel
class FeedViewModel(
  private val astrobinImageRepository: AstrobinImageRepository,
  private val pageAstrobinImagesUseCase: PageAstrobinImagesUseCase,
) : MviViewModel<FeedUiState, FeedPartialState, Nothing, FeedIntent>(
    FeedUiState(),
  ) {
  init {
    observeContinuousChanges(
      dependingOnState = { it.startFromInstant },
    ) { getAstrobinItems(it) }
  }

  override fun mapIntents(intent: FeedIntent): Flow<FeedPartialState> = when (intent) {
    is FeedIntent.StartFromMillisUTC -> flowOf { state ->
      // Query images shot earlier than the last moment of the day
      state.copy(
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

  override fun reduceUiState(
    previousState: FeedUiState,
    partialState: FeedPartialState,
  ): FeedUiState = partialState(previousState)

  private val retryFlow = MutableSharedFlow<Instant?>()

  private fun getAstrobinItems(startFromInstant: Instant?): Flow<FeedPartialState> =
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
      { state ->
        state.copy(
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

  private fun likeItem(item: PagedItem): Flow<FeedPartialState> = flow {
    astrobinImageRepository.setBookmarkedAndViewed(item.lastValidData!!.id, at = Clock.System.now())
  }

  private fun dislikeItem(item: PagedItem): Flow<FeedPartialState> = flow {
    astrobinImageRepository.setViewed(item.lastValidData!!.id, at = Clock.System.now())
  }
}
