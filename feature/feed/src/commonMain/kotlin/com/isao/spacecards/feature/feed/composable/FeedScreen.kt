package com.isao.spacecards.feature.feed.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.core.designsystem.theme.SpaceCardsTheme
import com.isao.spacecards.feature.common.util.PreviewLightDark
import com.isao.spacecards.feature.feed.FeedIntent
import com.isao.spacecards.feature.feed.FeedUiState
import com.isao.spacecards.feature.feed.FeedViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(viewModel: FeedViewModel = koinViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  FeedScreen(uiState = uiState, onIntent = viewModel::acceptIntent)
}

//TODO url for larger image on larger devices (fullSize or something)
//TODO app window gets cut off on fablets and tablets
//TODO display author and optional stuff on card
//TODO relying on db (isSeen) to hide seen items is not ideal
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
  uiState: FeedUiState,
  onIntent: (FeedIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val snackbarHostState = remember { SnackbarHostState() }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    // Let the content take up all available space.
    // Material3 components handle the insets themselves
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier,
  ) { padding ->
    FeedScreenContent(
      uiState,
      onIntent,
      Modifier
        .padding(padding)
        .fillMaxSize(),
    )
  }
}

@PreviewLightDark
@Composable
private fun FeedScreenPreview() {
  SpaceCardsTheme {
    FeedScreen(
      uiState = FeedUiState(
        items = flowOf(
          PagingData.from(
            List(2) {
              AstrobinImage(
                bookmarks = 1662,
                comments = 9344,
                dataSource = null,
                description = null,
                id = 4310,
                license = 1207,
                licenseName = null,
                likes = 9932,
                published = Clock.System.now(),
                title = null,
                updated = Clock.System.now(),
                uploaded = Clock.System.now(),
                urlGallery = null,
                urlHd = "",
                urlHistogram = null,
                urlReal = "",
                urlRegular = null,
                urlThumb = null,
                user = null,
                views = 4746,
                bookmarkedAt = null,
                viewedAt = null,
              )
            },
          ),
        ),
      ),
      onIntent = {},
    )
  }
}

@PreviewLightDark
@Composable
private fun FeedScreenLoadingPreview() {
  SpaceCardsTheme {
    FeedScreen(
      uiState = FeedUiState(
        items = flowOf(
          PagingData.empty(
            LoadStates(
              refresh = LoadState.NotLoading(endOfPaginationReached = true),
              prepend = LoadState.NotLoading(endOfPaginationReached = true),
              append = LoadState.Loading,
            ),
          ),
        ),
      ),
      onIntent = {},
    )
  }
}

@PreviewLightDark
@Composable
private fun FeedScreenNoItemsPreview() {
  SpaceCardsTheme {
    FeedScreen(
      uiState = FeedUiState(
        items = flowOf(
          PagingData.empty(
            LoadStates(
              refresh = LoadState.NotLoading(endOfPaginationReached = true),
              prepend = LoadState.NotLoading(endOfPaginationReached = true),
              append = LoadState.NotLoading(endOfPaginationReached = true),
            ),
          ),
        ),
      ),
      onIntent = {},
    )
  }
}

@PreviewLightDark
@Composable
private fun FeedScreenErrorPreview() {
  SpaceCardsTheme {
    FeedScreen(
      uiState = FeedUiState(
        items = flowOf(
          PagingData.empty(
            LoadStates(
              refresh = LoadState.NotLoading(endOfPaginationReached = true),
              prepend = LoadState.NotLoading(endOfPaginationReached = true),
              append = LoadState.Error(Exception()),
            ),
          ),
        ),
      ),
      onIntent = {},
    )
  }
}
