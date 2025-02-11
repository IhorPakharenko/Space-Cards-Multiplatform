package com.isao.spacecards.feature.feed.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.isao.spacecards.feature.common.extension.appendStringResourceWithStyles
import com.isao.spacecards.feature.common.extension.delayedWhen
import com.isao.spacecards.feature.common.extension.scale
import com.isao.spacecards.feature.designsystem.composable.dismissible.DismissDirection
import com.isao.spacecards.feature.designsystem.composable.dismissible.DismissibleState
import com.isao.spacecards.feature.designsystem.composable.dismissible.dismissible
import com.isao.spacecards.feature.designsystem.composable.dismissible.rememberDismissibleState
import com.isao.spacecards.feature.feed.FeedIntent
import com.isao.spacecards.feature.feed.FeedUiState
import com.isao.spacecards.feature.feed.FeedViewModel
import com.isao.spacecards.feature.feed.PagedItem
import com.isao.spacecards.foundation.ApiFailure
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.error_connection
import com.isao.spacecards.resources.error_image
import com.isao.spacecards.resources.error_server_astrobin
import com.isao.spacecards.resources.ok
import com.isao.spacecards.resources.retry
import com.isao.spacecards.resources.showing_outdated_content
import com.isao.spacecards.resources.space_on_date
import com.isao.spacecards.resources.youre_offline
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.min

@Composable
fun FeedRoute(viewModel: FeedViewModel = koinViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  FeedScreen(uiState = uiState, onIntent = viewModel::acceptIntent)
}

//TODO preload images
@Composable
private fun FeedScreen(
  uiState: FeedUiState,
  onIntent: (FeedIntent) -> Unit,
  modifier: Modifier = Modifier,
) = Scaffold(modifier = modifier) { padding ->
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(padding)
      .consumeWindowInsets(padding)
      .fillMaxSize(),
  ) {
    var isOffline by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(uiState.items) {
      // Don't decide if we're offline if no actual data has been loaded yet
      if (uiState.items.any { it.isLoading } || uiState.items.all { it.lastValidData == null }) {
        return@LaunchedEffect
      }
      isOffline = uiState.items.any { it.remoteFailure == ApiFailure.Connection }
    }

    var isDatePickerShown by rememberSaveable { mutableStateOf(false) }
    var isOfflineNoticeShown by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
      instant = uiState.items
        .getOrNull(0)
        ?.lastValidData
        ?.uploadedAt
        ?: uiState.startFromInstant
        ?: Clock.System.now(),
      isOffline = isOffline,
      onOfflinePinClick = { isOfflineNoticeShown = true },
      onDatePickerClick = { isDatePickerShown = true },
    )

    Box(
      Modifier
        .layout { measurable, constraints ->
          // Keep the width from exceeding the height, making it a square
          // on tablets and a card shape on phones.
          val placeable = measurable.measure(
            constraints.copy(
              maxWidth = min(constraints.maxWidth, constraints.maxHeight),
              minWidth = min(constraints.minWidth, constraints.minHeight),
            ),
          )
          layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
          }
        },
    ) {
      val topItem = uiState.items.getOrNull(0)
      val backgroundItem = uiState.items.getOrNull(1)

      var topPainterState by remember(topItem?.lastValidData?.id) {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
      }
      DisableSplashWhenFinished(topPainterState)
      val isLikeAllowed = topPainterState is AsyncImagePainter.State.Success
      val isDislikeAllowed = topPainterState != AsyncImagePainter.State.Empty

      val dismissibleState = key(topItem?.lastValidData?.id) {
        rememberDismissibleState(
          onDismiss = { direction ->
            onIntent(
              when (direction) {
                DismissDirection.Start -> FeedIntent.Dislike(topItem!!)
                DismissDirection.End -> FeedIntent.Like(topItem!!)
                else -> throw IllegalArgumentException()
              },
            )
          },
        )
      }

      val cardPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp)

      BackgroundCard(
        item = backgroundItem,
        dismissibleState = dismissibleState,
        modifier = Modifier
          .fillMaxSize()
          .padding(cardPadding),
      )

      TopCard(
        item = topItem,
        onPainterState = { topPainterState = it },
        onRetry = { onIntent(FeedIntent.Retry) },
        modifier = Modifier
          .fillMaxSize()
          .padding(cardPadding)
          .dismissible(
            state = dismissibleState,
            directions = setOfNotNull(
              DismissDirection.Start.takeIf { isDislikeAllowed },
              DismissDirection.End.takeIf { isLikeAllowed },
            ),
          ).then(
            // Fade is unnecessary for phones as the card will take up all screen
            if (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == COMPACT) {
              Modifier
            } else {
              Modifier.graphicsLayer {
                alpha = 1f - dismissibleState.combinedDismissProgress
              }
            },
          ),
      )

      FeedButtons(
        topItemState = dismissibleState,
        isLikeEnabled = isLikeAllowed,
        isDislikeEnabled = isDislikeAllowed,
        modifier = Modifier
          .padding(bottom = 48.dp)
          .align(Alignment.BottomCenter),
      )

      if (isDatePickerShown) {
        DatePicker(
          startFromInstant = uiState.startFromInstant,
          onDismiss = { isDatePickerShown = false },
          onPick = { onIntent(FeedIntent.StartFromMillisUTC(it)) },
        )
      }

      if (isOfflineNoticeShown) {
        OfflineModeDialog(
          onDismiss = { isOfflineNoticeShown = false },
          onRetry = { onIntent(FeedIntent.Retry) },
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
  instant: Instant,
  isOffline: Boolean,
  onOfflinePinClick: () -> Unit,
  onDatePickerClick: () -> Unit,
) {
  CenterAlignedTopAppBar(
    title = {
      val displayedDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
      val title = buildAnnotatedString {
        val date = displayedDate.date.format(
          LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            dayOfMonth()
          },
        )
        val dateStyle = SpanStyle(
          brush = Brush.linearGradient(
            listOf(
              Color(0xFFD76EF5),
              Color(0xFF5DD9FC),
            ),
          ),
        )
        appendStringResourceWithStyles(
          Res.string.space_on_date,
          date to dateStyle,
        )
      }
      Text(text = title, textAlign = TextAlign.Center)
    },
    navigationIcon = {
      if (isOffline) {
        IconButton(onClick = onOfflinePinClick) {
          Icon(Icons.Filled.OfflinePin, contentDescription = null)
        }
      }
    },
    actions = {
      IconButton(onClick = onDatePickerClick) {
        Icon(Icons.Filled.CalendarMonth, contentDescription = null)
      }
    },
    modifier = Modifier.clickable(onClick = onDatePickerClick),
  )
}

@Composable
private fun BackgroundCard(
  item: PagedItem?,
  dismissibleState: DismissibleState,
  modifier: Modifier = Modifier,
) {
  val backgroundCardScale = remember { Animatable(0f) }
  LaunchedEffect(dismissibleState) {
    snapshotFlow { dismissibleState.combinedDismissProgress }.collectLatest { progress ->
      val targetScale = progress.scale(
        oldMin = 0f, oldMax = 1f,
        newMin = 0.95f, newMax = 1f,
      )
      backgroundCardScale.animateTo(targetScale)
    }
  }
  val painter = key(item) {
    rememberAsyncImagePainter(item?.lastValidData?.urlHd)
  }
  val cardModifier = modifier
    .graphicsLayer {
      scaleX = backgroundCardScale.value
      scaleY = backgroundCardScale.value
    }
  val painterState by painter.state.collectAsState()
  when {
    painterState is AsyncImagePainter.State.Success && item?.lastValidData != null -> {
      FeedCard(
        item = item.lastValidData,
        painter = painter,
        modifier = cardModifier,
      )
    }

    painterState is AsyncImagePainter.State.Error -> {
      ErrorCard(modifier = cardModifier) {
        // No content for background image on error
      }
    }

    else -> {
      LoadingCard(cardModifier)
    }
  }
}

@Composable
@Suppress("ktlint:compose:modifier-reused-check") // False positive
private fun TopCard(
  item: PagedItem?,
  onPainterState: (AsyncImagePainter.State) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val movableLoadingCard = remember {
    movableContentOf { modifier: Modifier ->
      LoadingCard(modifier)
    }
  }

  when {
    //TODO we display item from local urgently, so when a new item is loaded
    // from remote later, the image may flicker. Display a tiny loading indicator somewhere?
    item?.lastValidData != null -> {
      val painter = rememberAsyncImagePainter(
        item.lastValidData.urlHd,
        transform = {
          it.also { onPainterState(it) }
        },
      )
      val delayedPainterState by painter.state.collectAsState().value.delayedWhen { _, new ->
        new is AsyncImagePainter.State.Loading
      }
      when (delayedPainterState) {
        is AsyncImagePainter.State.Success -> {
          FeedCard(
            item = item.lastValidData,
            painter = painter,
            modifier = modifier,
          )
        }

        is AsyncImagePainter.State.Error -> {
          ErrorCard(modifier = modifier) {
            Column(
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxSize().padding(16.dp),
            ) {
              Text(
                // I can't be bothered to research which throwables
                // in Coil Multiplatform correspond to particular errors
                text = stringResource(Res.string.error_image),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
              )
              Button(onClick = { painter.restart() }) {
                Text(stringResource(Res.string.retry))
              }
            }
          }
        }

        is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
          movableLoadingCard(modifier)
        }
      }
    }

    item?.remoteFailure != null -> ErrorCard(modifier = modifier) {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp),
      ) {
        Text(
          text = stringResource(
            when (item.remoteFailure) {
              ApiFailure.Connection -> Res.string.error_connection
              is ApiFailure.Serialization, is ApiFailure.Server ->
                Res.string.error_server_astrobin
            },
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(bottom = 16.dp),
        )
        Button(onClick = { onRetry() }) {
          Text(stringResource(Res.string.retry))
        }
      }
    }

    else -> movableLoadingCard(modifier)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
  startFromInstant: Instant?,
  onDismiss: () -> Unit,
  onPick: (Long) -> Unit,
) {
  val startFromDateTime = startFromInstant?.toLocalDateTime(TimeZone.currentSystemDefault())
  val nowDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
  val nowInstant = nowDateTime.toInstant(TimeZone.UTC)

  val datePickerState = rememberDatePickerState(
    initialSelectedDateMillis =
      (startFromDateTime?.toInstant(TimeZone.UTC) ?: nowInstant).toEpochMilliseconds(),
    yearRange = 2000..nowDateTime.year,
    selectableDates = object : SelectableDates {
      override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        nowInstant.toEpochMilliseconds() >= utcTimeMillis
    },
  )
  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(
        onClick = {
          onPick(datePickerState.selectedDateMillis!!)
          onDismiss()
        },
      ) { Text(stringResource(Res.string.ok)) }
    },
  ) {
    DatePicker(datePickerState)
  }
}

@Composable
fun OfflineModeDialog(
  onDismiss: () -> Unit,
  onRetry: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(stringResource(Res.string.youre_offline))
    },
    text = {
      Text(stringResource(Res.string.showing_outdated_content))
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(Res.string.ok))
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onRetry()
          onDismiss()
        },
      ) {
        Text(stringResource(Res.string.retry))
      }
    },
  )
}

@Composable
expect fun DisableSplashWhenFinished(painterState: AsyncImagePainter.State)

//TODO Previews for Android Studio are in early access:
// https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio
//@PreviewLightDark
//@Composable
//private fun FeedScreenPreview() {
//  SpaceCardsTheme {
//    FeedScreen(
//      uiState = FeedUiState(
//        items = flowOf(
//          PagingData.from(
//            List(2) {
//              AstrobinImage(
//                bookmarks = 1662,
//                comments = 9344,
//                dataSource = null,
//                description = null,
//                id = 4310,
//                license = 1207,
//                licenseName = null,
//                likes = 9932,
//                published = Clock.System.now(),
//                title = null,
//                updated = Clock.System.now(),
//                uploaded = Clock.System.now(),
//                urlGallery = null,
//                urlHd = "",
//                urlHistogram = null,
//                urlReal = "",
//                urlRegular = null,
//                urlThumb = null,
//                user = null,
//                views = 4746,
//                bookmarkedAt = null,
//                viewedAt = null,
//              )
//            },
//          ),
//        ),
//      ),
//      onIntent = {},
//    )
//  }
//}
//
//@PreviewLightDark
//@Composable
//private fun FeedScreenLoadingPreview() {
//  SpaceCardsTheme {
//    FeedScreen(
//      uiState = FeedUiState(
//        items = flowOf(
//          PagingData.empty(
//            LoadStates(
//              refresh = LoadState.NotLoading(endOfPaginationReached = true),
//              prepend = LoadState.NotLoading(endOfPaginationReached = true),
//              append = LoadState.Loading,
//            ),
//          ),
//        ),
//      ),
//      onIntent = {},
//    )
//  }
//}
//
//@PreviewLightDark
//@Composable
//private fun FeedScreenNoItemsPreview() {
//  SpaceCardsTheme {
//    FeedScreen(
//      uiState = FeedUiState(
//        items = flowOf(
//          PagingData.empty(
//            LoadStates(
//              refresh = LoadState.NotLoading(endOfPaginationReached = true),
//              prepend = LoadState.NotLoading(endOfPaginationReached = true),
//              append = LoadState.NotLoading(endOfPaginationReached = true),
//            ),
//          ),
//        ),
//      ),
//      onIntent = {},
//    )
//  }
//}
//
//@PreviewLightDark
//@Composable
//private fun FeedScreenErrorPreview() {
//  SpaceCardsTheme {
//    FeedScreen(
//      uiState = FeedUiState(
//        items = flowOf(
//          PagingData.empty(
//            LoadStates(
//              refresh = LoadState.NotLoading(endOfPaginationReached = true),
//              prepend = LoadState.NotLoading(endOfPaginationReached = true),
//              append = LoadState.Error(Exception()),
//            ),
//          ),
//        ),
//      ),
//      onIntent = {},
//    )
//  }
//}
