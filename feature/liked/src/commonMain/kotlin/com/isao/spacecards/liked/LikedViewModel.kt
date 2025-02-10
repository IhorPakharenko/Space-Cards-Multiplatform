package com.isao.spacecards.liked

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.PageBookmarkedAstrobinImagesUseCase
import com.isao.spacecards.feature.designsystem.MviViewModel
import com.isao.spacecards.liked.LikedViewModel.Keys.SHOULD_SORT_ASCENDING
import isao.pager.Config
import isao.pager.LoadOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal typealias LikedPartialState = (LikedUiState) -> LikedUiState

// Declare ViewModel in Koin Module until this issue is fixed:
// https://github.com/InsertKoinIO/koin-annotations/issues/185
// @KoinViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class LikedViewModel(
  private val observeImagesUseCase: PageBookmarkedAstrobinImagesUseCase,
  private val astrobinImageRepository: AstrobinImageRepository,
  savedStateHandle: SavedStateHandle,
) : MviViewModel<LikedUiState, LikedPartialState, LikedEvent, LikedIntent>(
    LikedUiState(
      shouldSortAscending = savedStateHandle[SHOULD_SORT_ASCENDING] ?: false,
    ),
  ) {
  init {
    observeContinuousChanges(
      uiStateSnapshot
        .map { it.shouldSortAscending }
        .distinctUntilChanged()
        .flatMapLatest { sortAscending ->
          getImages(sortAscending)
        },
    )

    viewModelScope.launch {
      uiStateSnapshot.collect { state ->
        savedStateHandle[SHOULD_SORT_ASCENDING] = state.shouldSortAscending
      }
    }
  }

  override fun mapIntents(intent: LikedIntent): Flow<LikedPartialState> = when (intent) {
    is LikedIntent.SetSorting -> flowOf { state ->
      state.copy(shouldSortAscending = intent.shouldSortAscending)
    }
    is LikedIntent.PageReached -> flowOf { state ->
      state.copy(currentPage = intent.page)
    }
    is LikedIntent.ImageClicked -> itemClicked(intent.item)
    is LikedIntent.DeleteImageClicked -> deleteImageClicked(intent.item)
    is LikedIntent.AuthorClicked -> authorClicked(intent.item)
  }

  override fun reduceUiState(
    previousState: LikedUiState,
    partialState: LikedPartialState,
  ): LikedUiState = partialState(previousState)

  private fun getImages(sortAscending: Boolean): Flow<LikedPartialState> = flow {
    //TODO LaunchedEffect for updating currentPage does not trigger instantly,
    // so we have to reset currentPage ourselves when changing the whole list
    // (e.g. changing sort order). Can this be done in a better way?
    emit { state -> state.copy(currentPage = 0) }
    emitAll(
      observeImagesUseCase(
        shouldSortAscending = sortAscending,
        config = Config(
          loadOrder = LoadOrder.LocalOnly,
          pageSize = 40,
          initialRequestKey = 0,
        ),
        currentPage = uiStateSnapshot.map { it.currentPage }.distinctUntilChanged(),
      ).map { pages ->
        { state ->
          state.copy(
            items = pages.flatMap { page ->
              page.lastValidItems.map { PagedItem(page.key, it) }
            },
            isLoading = pages.any { it.isLoading },
          )
        }
      },
    )
  }

  private fun itemClicked(item: PagedItem): Flow<LikedPartialState> {
    publishEvent(LikedEvent.OpenWebBrowser(item.astrobinImage.urlPost))
    return emptyFlow()
  }

  private fun deleteImageClicked(item: PagedItem): Flow<LikedPartialState> = flow {
    astrobinImageRepository.setBookmarked(item.astrobinImage.id, at = null)
  }

  private fun authorClicked(item: PagedItem): Flow<LikedPartialState> {
    publishEvent(LikedEvent.OpenWebBrowser(item.astrobinImage.urlAuthor))
    return emptyFlow()
  }

  object Keys {
    const val SHOULD_SORT_ASCENDING = "shouldSortAscending"
  }
}
