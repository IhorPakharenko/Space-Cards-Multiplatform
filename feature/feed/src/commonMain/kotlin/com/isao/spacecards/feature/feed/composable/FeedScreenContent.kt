package com.isao.spacecards.feature.feed.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImagePainter
import com.isao.spacecards.core.designsystem.composable.dismissible.DismissDirection
import com.isao.spacecards.core.designsystem.composable.dismissible.DismissibleState
import com.isao.spacecards.core.designsystem.composable.dismissible.dismissible
import com.isao.spacecards.core.designsystem.composable.dismissible.rememberDismissibleState
import com.isao.spacecards.feature.common.extension.scale
import com.isao.spacecards.feature.feed.FeedIntent
import com.isao.spacecards.feature.feed.FeedUiState
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.like
import com.isao.spacecards.resources.nope
import com.isao.spacecards.resources.ok
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun FeedScreenContent(
  uiState: FeedUiState,
  onIntent: (FeedIntent) -> Unit,
  modifier: Modifier = Modifier,
) = Column(modifier) {
  val pagingItems = uiState.items.collectAsLazyPagingItems()

  //TODO error handling
//  LaunchedEffect(pagingItems) {
//    snapshotFlow { pagingItems.loadState }.collect { loadState ->
//      loadState.appendErrorOrNull()?.let { snackbarHostState.showSnackbar(it.message) }
//      loadState.refreshErrorOrNull()?.let { snackbarHostState.showSnackbar(it.message) }
//    }
//  }

  //TODO loading handling
  val refreshing by remember(pagingItems) {
    derivedStateOf { pagingItems.loadState.refresh == LoadState.Loading }
  }

  var isPickingDate by rememberSaveable { mutableStateOf(false) }

  // Let the card be drawn above the app bar
  CenterAlignedTopAppBar(
    title = {
      val topItem = if (pagingItems.itemCount > 0) pagingItems.peek(0) else null
      val topItemUploadedInstant = topItem?.uploaded
      val selectedInstant = uiState.startFromInstant
      val now = Clock.System.now()
      val displayedInstant = when {
        topItemUploadedInstant != null -> topItemUploadedInstant
        selectedInstant != null -> selectedInstant
        else -> now
      }

      Text(
        displayedInstant.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
        Modifier.clickable {
          isPickingDate = true
        },
      )
    },
  )

  BoxWithConstraints {
    val scope = rememberCoroutineScope()

    val backgroundItem = if (pagingItems.itemCount > 1) pagingItems[1] else null
    val topItem = if (pagingItems.itemCount > 0) pagingItems[0] else null

    val topItemState = rememberDismissibleState(
      onDismiss = { direction ->
        topItem ?: return@rememberDismissibleState
        onIntent(
          when (direction) {
            DismissDirection.Start -> FeedIntent.Dislike(topItem)
            DismissDirection.End -> FeedIntent.Like(topItem)
            else -> throw IllegalArgumentException()
          },
        )
        scope.launch {
          reset(null)
        }
      },
    )

    val horizontalCardPadding = 16.dp
    val verticalCardPadding = 32.dp
    val cardPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp)
    val cardImageWidth = maxWidth - horizontalCardPadding * 2
    val cardImageHeight = maxHeight - verticalCardPadding * 2

    PreloadImages(
      items = pagingItems.itemSnapshotList
        .filterNotNull()
        .map { it.urlHd }
        .drop(2) // Top and background items don't need to be loaded twice
        .toSet(),
      width = cardImageWidth,
      height = cardImageHeight,
    )

    if (backgroundItem != null) {
      // Defer animated scale reads to the graphicsLayer block, avoiding recompositions on card drag
      val backgroundCardScale = remember { Animatable(0f) }
      LaunchedEffect(Unit) {
        snapshotFlow { topItemState.combinedDismissProgress }.collectLatest { progress ->
          val targetScale = progress
            .coerceIn(0f..1f)
            .scale(
              oldMin = 0f, oldMax = 1f,
              newMin = 0.95f, newMax = 1f,
            )
          backgroundCardScale.animateTo(targetScale)
        }
      }

      FeedCard(
        item = backgroundItem,
        width = cardImageWidth,
        height = cardImageHeight,
        Modifier
          .padding(cardPadding)
          .graphicsLayer {
            scaleX = backgroundCardScale.value
            scaleY = backgroundCardScale.value
          },
      )
    }

    val topItemPainter = key(topItem) {
      topItem?.let { item ->
        FeedCardDefaults
          .rememberRetryingAsyncImagePainter(
            item = item,
            width = cardImageWidth,
            height = cardImageHeight,
          )
      }
    }
    val topItemPainterState
      by (topItemPainter?.state ?: MutableStateFlow(AsyncImagePainter.State.Empty)).collectAsState()

    SplashController(painterState = topItemPainterState)

    val isLikeAllowed by remember(topItem) {
      derivedStateOf {
        topItemPainterState is AsyncImagePainter.State.Success
      }
    }
    val isDislikeAllowed by remember(topItem) {
      derivedStateOf {
        topItemPainterState != AsyncImagePainter.State.Empty
      }
    }

    key(topItem) {
      FeedCard(
        painter = topItemPainter,
        modifier = Modifier
          .padding(cardPadding)
          .dismissible(
            state = topItemState,
            directions = setOfNotNull(
              DismissDirection.Start.takeIf { isDislikeAllowed },
              DismissDirection.End.takeIf { isLikeAllowed },
            ),
          ),
      )
    }

    FeedButtons(
      topItemState = topItemState,
      isLikeEnabled = isLikeAllowed,
      isDislikeEnabled = isDislikeAllowed,
      modifier = Modifier
        .padding(bottom = 48.dp)
        .align(Alignment.BottomCenter),
    )

    DatePicker(
      isPickingDate = isPickingDate,
      startFromInstant = uiState.startFromInstant,
      onDismiss = { isPickingDate = false },
      onPick = { onIntent(FeedIntent.StartFromInstant(it)) },
    )
  }
}

@Composable
private fun FeedButtons(
  topItemState: DismissibleState,
  isLikeEnabled: Boolean,
  isDislikeEnabled: Boolean,
  modifier: Modifier = Modifier,
) = Row(modifier) {
  val scope = rememberCoroutineScope()

  // Defer animated scale reads to the graphicsLayer block, avoiding recompositions on card drag
  val dislikeButtonScale = remember { Animatable(0f) }
  val likeButtonScale = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    snapshotFlow { topItemState.horizontalDismissProgress }.collectLatest { progress ->
      val dislikeTargetScale = getButtonScale(progress * -1)
      val likeTargetScale = getButtonScale(progress)
      launch { dislikeButtonScale.animateTo(dislikeTargetScale) }
      launch { likeButtonScale.animateTo(likeTargetScale) }
    }
  }

  // Dislike button
  FeedButton(
    onClick = {
      scope.launch {
        if (topItemState.dismissDirection != null) return@launch
        launch { topItemState.dismiss(DismissDirection.Start) }
      }
    },
    modifier = Modifier.graphicsLayer {
      scaleX = dislikeButtonScale.value
      scaleY = dislikeButtonScale.value
    },
    enabled = isDislikeEnabled,
  ) {
    DislikeIcon()
  }

  Spacer(Modifier.width(72.dp))

  // Like button
  FeedButton(
    onClick = {
      scope.launch {
        if (topItemState.dismissDirection != null) return@launch
        launch { topItemState.dismiss(DismissDirection.End) }
      }
    },
    modifier = Modifier.graphicsLayer {
      scaleX = likeButtonScale.value
      scaleY = likeButtonScale.value
    },
    enabled = isLikeEnabled,
  ) {
    LikeIcon()
  }
}

@Composable
private fun FeedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable () -> Unit,
) {
  val containerColor by animateColorAsState(
    if (enabled) Color.Black.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f),
    animationSpec = tween(600),
  )
  val contentAndOutlineColor by animateColorAsState(
    if (enabled) Color.White else Color.White.copy(alpha = 0.3f),
    animationSpec = tween(400),
  )
  OutlinedButton(
    onClick = onClick,
    modifier = modifier.size(80.dp),
    enabled = enabled,
    shape = CircleShape,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = containerColor,
      contentColor = contentAndOutlineColor,
      disabledContainerColor = containerColor,
      disabledContentColor = contentAndOutlineColor,
    ),
    border = BorderStroke(
      width = 2.dp,
      color = contentAndOutlineColor,
    ),
  ) {
    content()
  }
}

@Composable
private fun DislikeIcon(modifier: Modifier = Modifier) {
  Icon(
    imageVector = Icons.Rounded.Close,
    contentDescription = stringResource(Res.string.nope),
    modifier = modifier.size(32.dp),
  )
}

@Composable
private fun LikeIcon(modifier: Modifier = Modifier) {
  Icon(
    imageVector = Icons.Rounded.Favorite,
    contentDescription = stringResource(Res.string.like),
    modifier = modifier.size(32.dp),
  )
}

private fun getButtonScale(dismissProgress: Float): Float {
  val minProgress = 0f
  val maxProgress = 0.5f
  val minScale = 0.8f
  val maxScale = 1f

  if (dismissProgress.isNaN()) return minScale // Dismiss progress has not been initialized yet

  return when {
    dismissProgress < minProgress -> minScale
    dismissProgress > maxProgress -> maxScale
    else -> dismissProgress.scale(
      oldMin = minProgress, oldMax = maxProgress,
      newMin = minScale, newMax = maxScale,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
  isPickingDate: Boolean,
  startFromInstant: Instant?,
  onDismiss: () -> Unit,
  onPick: (Instant) -> Unit,
) {
  if (!isPickingDate) return

  val now = Clock.System.now()
  val currentYear = Clock.System
    .now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .year

  val datePickerState = rememberDatePickerState(
    initialSelectedDateMillis = (startFromInstant ?: now).toEpochMilliseconds(),
    initialDisplayedMonthMillis = null,
    yearRange = 2000..currentYear,
    selectableDates = object : SelectableDates {
      override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        now.toEpochMilliseconds() >= utcTimeMillis
    },
  )
  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(
        onClick = {
          val picked = Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!)
          onPick(picked)
          onDismiss()
        },
      ) { Text(stringResource(Res.string.ok)) }
    },
  ) {
    androidx.compose.material3.DatePicker(datePickerState)
  }
}

@Composable
expect fun SplashController(painterState: AsyncImagePainter.State)
