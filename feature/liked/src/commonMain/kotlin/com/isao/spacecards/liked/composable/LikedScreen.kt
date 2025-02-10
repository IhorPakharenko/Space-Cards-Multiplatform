package com.isao.spacecards.liked.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import androidx.window.core.layout.WindowWidthSizeClass.Companion.MEDIUM
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.feature.common.extension.collectWithLifecycle
import com.isao.spacecards.feature.common.extension.delayedWhen
import com.isao.spacecards.feature.common.util.PreviewLightDark
import com.isao.spacecards.feature.designsystem.theme.SpaceCardsTheme
import com.isao.spacecards.liked.LikedEvent
import com.isao.spacecards.liked.LikedIntent
import com.isao.spacecards.liked.LikedUiState
import com.isao.spacecards.liked.LikedViewModel
import com.isao.spacecards.liked.PagedItem
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.delete
import com.isao.spacecards.resources.image_by
import com.isao.spacecards.resources.like_images_to_view_them_here
import com.isao.spacecards.resources.liked
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LikedRoute(viewModel: LikedViewModel = koinViewModel()) {
  HandleEvents(viewModel.event)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LikedScreen(uiState = uiState, onIntent = viewModel::acceptIntent)
}

@Composable
private fun HandleEvents(events: Flow<LikedEvent>) {
  val uriHandler = LocalUriHandler.current

  events.collectWithLifecycle {
    when (it) {
      is LikedEvent.OpenWebBrowser -> {
        uriHandler.openUri(it.uri)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LikedScreen(
  uiState: LikedUiState,
  onIntent: (LikedIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val topBarState = rememberTopAppBarState()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

  Scaffold(
    modifier = modifier,
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(stringResource(Res.string.liked))
        },
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    if (uiState.items.isEmpty() && !uiState.isLoading) {
      NoItemsPlaceholder()
    } else {
      ItemsAvailableContent(
        uiState = uiState,
        onIntent = onIntent,
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
          .nestedScroll(scrollBehavior.nestedScrollConnection),
      )
    }
  }
}

// Restarting the image request after regaining network connectivity is an open issue
// in coil since 2019: https://github.com/coil-kt/coil/issues/132
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsAvailableContent(
  uiState: LikedUiState,
  onIntent: (LikedIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedItem by rememberSaveable { mutableStateOf<PagedItem?>(null) }

  val gridState = rememberLazyGridState()
  LaunchedEffect(onIntent, uiState.items) {
    snapshotFlow {
      val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo
        .lastOrNull()
        ?.index ?: 0
      uiState.items.getOrNull(lastVisibleIndex - 1)?.page ?: 0
    }.collect { page ->
      onIntent(
        LikedIntent.PageReached(page),
      )
    }
  }

  val delayedItems by uiState.items.delayedWhen { old, new -> old.size > new.size }
  LazyVerticalGrid(
    columns = GridCells.Fixed(
      when (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
        COMPACT -> 2
        MEDIUM -> 3
        else -> 4
      },
    ),
    modifier = modifier,
    state = gridState,
    contentPadding = when (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
      COMPACT -> PaddingValues(bottom = 2.dp)
      else -> PaddingValues(4.dp)
    },
    verticalArrangement = Arrangement.spacedBy(2.dp),
    horizontalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    item(
      span = {
        GridItemSpan(this.maxLineSpan)
      },
    ) {
      LikedGridSettings(
        sortAscending = uiState.shouldSortAscending,
        setSortAscending = { onIntent(LikedIntent.SetSorting(it)) },
      )
    }

    items(delayedItems, key = { it.astrobinImage.id }) { item ->
      Box(
        Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .animateItem(),
      ) {
        val isSelected by remember { derivedStateOf { selectedItem == item } }
        val sizeFraction by animateFloatAsState(if (isSelected) 0.85f else 1f)
        LikedItem(
          item = item,
          onClick = { onIntent(LikedIntent.ImageClicked(item)) },
          onLongClick = { selectedItem = item },
          modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
            .graphicsLayer {
              scaleX = sizeFraction
              scaleY = sizeFraction
            },
        )
        ImageActionsPopup(
          expanded = isSelected,
          item = item,
          onDismissRequest = { selectedItem = null },
          onSourceClick = { onIntent(LikedIntent.AuthorClicked(item)) },
          onDeleteClick = { onIntent(LikedIntent.DeleteImageClicked(item)) },
        )
      }
    }
  }
}

@Composable
private fun ImageActionsPopup(
  expanded: Boolean,
  item: PagedItem,
  onDismissRequest: () -> Unit,
  onSourceClick: () -> Unit,
  onDeleteClick: () -> Unit,
) {
  DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismissRequest,
    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
  ) {
    ClickableText(
      text = buildAnnotatedString {
        val authorName = item.astrobinImage.user
        val fullString = stringResource(Res.string.image_by, authorName)
        val websiteStart = fullString.indexOf(authorName)
        val websiteEnd = websiteStart + authorName.length

        append(fullString)

        addStyle(
          style = SpanStyle(
            color = MaterialTheme.colorScheme.onSurface,
          ),
          start = 0,
          end = fullString.length,
        )
        addStyle(
          style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
          ),
          start = websiteStart,
          end = websiteEnd,
        )
      },
      modifier = Modifier.padding(16.dp),
      style = MaterialTheme.typography.titleMedium,
      onClick = {
        onSourceClick()
        onDismissRequest()
      },
    )
    Spacer(Modifier.height(24.dp))
    DropdownMenuItem(
      text = {
        Text(text = stringResource(Res.string.delete))
      },
      onClick = {
        onDeleteClick()
        onDismissRequest()
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Outlined.Delete,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurface,
        )
      },
    )
  }
}

@Composable
private fun NoItemsPlaceholder(modifier: Modifier = Modifier) {
  Box(
    modifier
      .background(MaterialTheme.colorScheme.background)
      .fillMaxSize()
      .padding(32.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      stringResource(Res.string.like_images_to_view_them_here),
      color = MaterialTheme.colorScheme.onBackground,
      style = MaterialTheme.typography.headlineMedium,
      textAlign = TextAlign.Center,
    )
  }
}

//TODO Previews for Android Studio are in early access:
// https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio
@PreviewLightDark
@Composable
private fun LikedScreenPreview() {
  SpaceCardsTheme {
    LikedScreen(
      uiState = LikedUiState(
        items = List(50) {
          PagedItem(
            page = 0,
            astrobinImage = AstrobinImage(
              id = 7935,
              user = "a",
              title = null,
              description = null,
              bookmarks = 8425,
              comments = 4055,
              dataSource = null,
              license = 5508,
              licenseName = null,
              likes = 9579,
              publishedAt = Clock.System.now(),
              updatedAt = Clock.System.now(),
              uploadedAt = Clock.System.now(),
              urlGallery = null,
              urlHd = "https://duckduckgo.com/?q=integer",
              urlHistogram = null,
              urlReal = "http://www.bing.com/search?q=quem",
              urlRegular = null,
              urlThumb = null,
              views = 7025,
              bookmarkedAt = null,
              viewedAt = null,
            ),
          )
        },
      ),
      onIntent = {},
    )
  }
}

@PreviewLightDark
@Composable
private fun LikedScreenLoadingPreview() {
  SpaceCardsTheme {
    LikedScreen(
      uiState = LikedUiState(
        isLoading = true,
      ),
      onIntent = {},
    )
  }
}

@PreviewLightDark
@Composable
private fun LikedScreenNoItemsPreview() {
  SpaceCardsTheme {
    LikedScreen(
      uiState = LikedUiState(),
      onIntent = {},
    )
  }
}
