package com.isao.spacecards.feature.feed.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isao.spacecards.component.images.domain.model.ImageSource
import com.isao.spacecards.core.designsystem.LocalWindowSizeClass
import com.isao.spacecards.core.designsystem.composable.dismissible.DismissibleStack
import com.isao.spacecards.core.designsystem.theme.SpaceCardsTheme
import com.isao.spacecards.feature.common.util.PreviewLightDark
import com.isao.spacecards.feature.feed.FeedIntent
import com.isao.spacecards.feature.feed.FeedUiState
import com.isao.spacecards.feature.feed.FeedViewModel
import com.isao.spacecards.feature.feed.model.FeedItemDisplayable
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.app_name
import com.isao.spacecards.resources.something_went_wrong
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(viewModel: FeedViewModel = koinViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  FeedScreen(uiState = uiState, onIntent = viewModel::acceptIntent)
}

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
    Column {
      // Let the card be drawn above the app bar
      CenterAlignedTopAppBar(
        title = {
          Text(stringResource(Res.string.app_name))
        },
      )

      if (uiState.isError) {
        val errorMessage = stringResource(Res.string.something_went_wrong)

        LaunchedEffect(snackbarHostState) {
          snackbarHostState.showSnackbar(
            message = errorMessage,
          )
        }
      }
//        FeedScreenContent(
//            uiState,
//            onIntent,
//            modifier
//                .padding(padding)
//                .fillMaxSize()
//        )
      val cardSizeModifier =
        if (LocalWindowSizeClass.current.widthSizeClass != WindowWidthSizeClass.Compact) {
          Modifier.fillMaxHeight().aspectRatio(0.7f)
        } else {
          Modifier.fillMaxSize()
        }
      DismissibleStack(
        uiState.items,
        content = {
          FeedCard(item = it, Modifier.then(cardSizeModifier).padding(16.dp))
        },
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun FeedScreenPreview() {
  SpaceCardsTheme {
    FeedScreen(
      uiState = FeedUiState(
        items = List(2) {
          FeedItemDisplayable(
            id = it.toString(),
            imageId = "",
            source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST,
            imageUrl = "",
            sourceUrl = "",
          )
        },
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
        isLoading = true,
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
      uiState = FeedUiState(),
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
        isError = true,
      ),
      onIntent = {},
    )
  }
}
