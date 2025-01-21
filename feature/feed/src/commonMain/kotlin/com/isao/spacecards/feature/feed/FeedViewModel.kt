package com.isao.spacecards.feature.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.ObservePagedAstrobinImagesUseCase
import com.isao.spacecards.core.designsystem.MviViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

// Declare ViewModel in Koin Module until this issue is fixed:
// https://github.com/InsertKoinIO/koin-annotations/issues/185
// @KoinViewModel
class FeedViewModel(
  private val astrobinImageRepository: AstrobinImageRepository,
  private val observePagedAstrobinImagesUseCase: ObservePagedAstrobinImagesUseCase,
) : MviViewModel<FeedUiState, FeedPartialState, Nothing, FeedIntent>(
    FeedUiState(),
  ) {
  init {
    observeContinuousChanges(
      dependingOnState = { it.startFromInstant },
    ) { getAstrobinItems(it) }
  }

  override fun mapIntents(intent: FeedIntent): Flow<FeedPartialState> = when (intent) {
    is FeedIntent.StartFromInstant -> flowOf(FeedPartialState.StartFromInstantSet(intent.instant))
    is FeedIntent.Like -> {
      likeItem(intent.item)
    }

    is FeedIntent.Dislike -> {
      dislikeItem(intent.item)
    }
  }

  override fun reduceUiState(
    previousState: FeedUiState,
    partialState: FeedPartialState,
  ): FeedUiState = when (partialState) {
    is FeedPartialState.ItemsFetched -> previousState.copy(
      items = partialState.items,
    )

    is FeedPartialState.StartFromInstantSet -> previousState.copy(
      startFromInstant = partialState.instant,
    )
  }

  private fun getAstrobinItems(startFromInstant: Instant?): Flow<FeedPartialState> = flow {
    val pagingFlow = observePagedAstrobinImagesUseCase(
      config = PagingConfig(pageSize = 20),
      startFromInstantExclusive = startFromInstant,
    ).cachedIn(viewModelScope) //TODO why is this needed?
    emit(FeedPartialState.ItemsFetched(pagingFlow))
  }

  private fun likeItem(item: AstrobinImage): Flow<FeedPartialState> = flow {
    astrobinImageRepository.setBookmarked(item.id, at = Clock.System.now())
  }

  private fun dislikeItem(item: AstrobinImage): Flow<FeedPartialState> = flow {
    astrobinImageRepository.setViewed(item.id, at = Clock.System.now())
  }
}
