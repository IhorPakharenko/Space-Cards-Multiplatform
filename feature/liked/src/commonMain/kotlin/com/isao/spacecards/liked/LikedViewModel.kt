package com.isao.spacecards.liked

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.PageBookmarkedAstrobinImagesUseCase
import com.isao.spacecards.feature.designsystem.MviViewModel
import com.isao.spacecards.feature.designsystem.Reducer
import com.isao.spacecards.liked.LikedViewModel.Keys.SHOULD_SORT_ASCENDING
import isao.pager.Config
import isao.pager.LoadOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Declare ViewModel in Koin Module until this issue is fixed:
// https://github.com/InsertKoinIO/koin-annotations/issues/185
// @KoinViewModel
class LikedViewModel(
  private val observeImagesUseCase: PageBookmarkedAstrobinImagesUseCase,
  private val astrobinImageRepository: AstrobinImageRepository,
  savedStateHandle: SavedStateHandle,
) : MviViewModel<LikedUiState, LikedEvent, LikedIntent>(
    LikedUiState(
      shouldSortAscending = savedStateHandle[SHOULD_SORT_ASCENDING] ?: false,
    ),
  ) {
  init {
    observeContinuousChanges(
      dependingOnState = { it.shouldSortAscending },
    ) { getImages(it) }

    viewModelScope.launch {
      uiStateSnapshot.collect { state ->
        savedStateHandle[SHOULD_SORT_ASCENDING] = state.shouldSortAscending
      }
    }
  }

  override fun mapIntents(intent: LikedIntent): Flow<Reducer<LikedUiState>> = when (intent) {
    is LikedIntent.SetSorting -> flowOfUpdateState {
      copy(shouldSortAscending = intent.shouldSortAscending)
    }

    is LikedIntent.PageReached -> flowOfUpdateState {
      copy(currentPage = intent.page)
    }
    is LikedIntent.ImageClicked -> itemClicked(intent.item)
    is LikedIntent.DeleteImageClicked -> deleteImageClicked(intent.item)
    is LikedIntent.AuthorClicked -> authorClicked(intent.item)
  }

  private fun getImages(sortAscending: Boolean): Flow<Reducer<LikedUiState>> = flow {
    //TODO LaunchedEffect for updating currentPage does not trigger instantly,
    // so we have to reset currentPage ourselves when changing the whole list
    // (e.g. changing sort order). Can this be done in a better way?
    emit(updateState { copy(currentPage = 0) })
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
        updateState {
          copy(
            items = pages.flatMap { page ->
              page.lastValidItems.map { PagedItem(page.key, it) }
            },
            isLoading = pages.any { it.isLoading },
          )
        }
      },
    )
  }

  private fun itemClicked(item: PagedItem): Flow<Reducer<LikedUiState>> = flow {
    publishEvent(LikedEvent.OpenWebBrowser(item.astrobinImage.urlPost))
  }

  private fun deleteImageClicked(item: PagedItem): Flow<Reducer<LikedUiState>> = flow {
    astrobinImageRepository.setBookmarked(item.astrobinImage.id, at = null)
  }

  private fun authorClicked(item: PagedItem): Flow<Reducer<LikedUiState>> = flow {
    publishEvent(LikedEvent.OpenWebBrowser(item.astrobinImage.urlAuthor))
  }

  object Keys {
    const val SHOULD_SORT_ASCENDING = "shouldSortAscending"
  }
}
