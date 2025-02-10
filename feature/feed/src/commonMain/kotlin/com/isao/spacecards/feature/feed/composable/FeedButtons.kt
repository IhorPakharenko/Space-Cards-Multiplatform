package com.isao.spacecards.feature.feed.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.isao.spacecards.feature.common.extension.scale
import com.isao.spacecards.feature.designsystem.composable.dismissible.DismissDirection
import com.isao.spacecards.feature.designsystem.composable.dismissible.DismissibleState
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.like
import com.isao.spacecards.resources.nope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FeedButtons(
  topItemState: DismissibleState,
  isLikeEnabled: Boolean,
  isDislikeEnabled: Boolean,
  modifier: Modifier = Modifier,
) = Row(modifier) {
  val scope = rememberCoroutineScope()
  // Defer animated scale reads to the graphicsLayer block, avoiding recompositions on card drag
  val dislikeButtonScale = remember { Animatable(1f) }
  val likeButtonScale = remember { Animatable(1f) }
  LaunchedEffect(topItemState) {
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
    Icon(
      imageVector = Icons.Rounded.Close,
      contentDescription = stringResource(Res.string.nope),
      modifier = Modifier.size(32.dp),
    )
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
    Icon(
      imageVector = Icons.Rounded.Favorite,
      contentDescription = stringResource(Res.string.like),
      modifier = Modifier.size(32.dp),
    )
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

private fun getButtonScale(dismissProgress: Float): Float {
  val minProgress = 0f
  val maxProgress = 0.5f
  val minScale = 0.8f
  val maxScale = 1f

  if (dismissProgress.isNaN()) return minScale // Dismiss progress has not been initialized yet

  return dismissProgress
    .coerceIn(minProgress..maxProgress)
    .scale(
      oldMin = minProgress, oldMax = maxProgress,
      newMin = minScale, newMax = maxScale,
    )
}
