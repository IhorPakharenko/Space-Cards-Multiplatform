package com.isao.yfoo3.presentation.feed.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isao.yfoo3.core.theme.Yfoo2Theme
import com.isao.yfoo3.core.utils.PreviewLightDark
import com.isao.yfoo3.data.model.ImageSource
import com.isao.yfoo3.presentation.feed.FeedIntent
import com.isao.yfoo3.presentation.feed.FeedUiState
import com.isao.yfoo3.presentation.feed.FeedViewModel
import com.isao.yfoo3.presentation.feed.model.FeedItemDisplayable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yfoomultiplatform.composeapp.generated.resources.Res
import yfoomultiplatform.composeapp.generated.resources.app_name
import yfoomultiplatform.composeapp.generated.resources.something_went_wrong

@Composable
fun FeedRoute(
    viewModel: FeedViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FeedScreen(uiState = uiState, onIntent = viewModel::acceptIntent)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    uiState: FeedUiState,
    onIntent: (FeedIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    //TODO display the card above the app bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.app_name))
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Let the content take up all available space.
        // Material3 components handle the insets themselves
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        if (uiState.isError) {
            val errorMessage = stringResource(Res.string.something_went_wrong)

            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                )
            }
        }
        FeedScreenContent(
            uiState,
            onIntent,
            modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

@PreviewLightDark
@Composable
private fun FeedScreenPreview() {
    Yfoo2Theme {
        FeedScreen(
            uiState = FeedUiState(
                items = List(2) {
                    FeedItemDisplayable(
                        id = it.toString(),
                        imageId = "",
                        source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST,
                        imageUrl = "",
                        sourceUrl = ""
                    )
                }
            ),
            onIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun FeedScreenLoadingPreview() {
    Yfoo2Theme {
        FeedScreen(
            uiState = FeedUiState(
                isLoading = true
            ),
            onIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun FeedScreenNoItemsPreview() {
    Yfoo2Theme {
        FeedScreen(
            uiState = FeedUiState(),
            onIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun FeedScreenErrorPreview() {
    Yfoo2Theme {
        FeedScreen(
            uiState = FeedUiState(
                isError = true
            ),
            onIntent = {}
        )
    }
}
