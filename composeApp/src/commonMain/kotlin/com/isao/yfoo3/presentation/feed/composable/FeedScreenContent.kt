package com.isao.yfoo3.presentation.feed.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.isao.yfoo3.core.extensions.scale
import com.isao.yfoo3.presentation.composable.dismissible.DismissDirection
import com.isao.yfoo3.presentation.composable.dismissible.DismissibleState
import com.isao.yfoo3.presentation.composable.dismissible.dismissible
import com.isao.yfoo3.presentation.composable.dismissible.rememberDismissibleState
import com.isao.yfoo3.presentation.feed.FeedIntent
import com.isao.yfoo3.presentation.feed.FeedUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import yfoomultiplatform.composeapp.generated.resources.Res
import yfoomultiplatform.composeapp.generated.resources.like
import yfoomultiplatform.composeapp.generated.resources.nope

@ExperimentalMaterialApi
@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    onIntent: (FeedIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val scope = rememberCoroutineScope()

        val preloadedItem = uiState.items.getOrNull(2)
        val backgroundItem = uiState.items.getOrNull(1)
        val topItem = uiState.items.getOrNull(0)

        val topItemState = rememberDismissibleState(
            onDismiss = { direction ->
                topItem ?: return@rememberDismissibleState
                onIntent(
                    when (direction) {
                        DismissDirection.Start -> FeedIntent.Dislike(topItem)
                        DismissDirection.End -> FeedIntent.Like(topItem)
                        else -> throw IllegalArgumentException()
                    }
                )
                scope.launch {
                    reset(null)
                }
            }
        )

        val horizontalCardPadding = 16.dp
        val verticalCardPadding = 32.dp
        val cardPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp)
        val cardImageWidth = maxWidth - horizontalCardPadding * 2
        val cardImageHeight = maxHeight - verticalCardPadding * 2

        PreloadFeedItem(
            item = preloadedItem,
            width = cardImageWidth,
            height = cardImageHeight
        )

        if (backgroundItem != null) {
            val backgroundCardTargetScale by remember {
                derivedStateOf {
                    topItemState.combinedDismissProgress
                        .coerceIn(0f..1f)
                        .scale(
                            oldMin = 0f, oldMax = 1f,
                            newMin = 0.95f, newMax = 1f,
                        )
                }
            }
            val backgroundCardScale by animateFloatAsState(backgroundCardTargetScale)

            FeedCard(
                item = backgroundItem,
                width = cardImageWidth,
                height = cardImageHeight,
                Modifier
                    .padding(cardPadding)
                    .graphicsLayer {
                        scaleX = backgroundCardScale
                        scaleY = backgroundCardScale
                    }
            )
        }
        val topItemPainter = key(topItem) {
            topItem?.let { item ->
                FeedCardDefaults.rememberRetryingAsyncImagePainter(
                    item = item,
                    width = cardImageWidth,
                    height = cardImageHeight
                ).also {
                    SplashController(painterState = it.state.collectAsState().value)
                }
            }
        }
        val topItemPainterState
                by (topItemPainter?.state ?: MutableStateFlow(null)).collectAsState()

        val isLikeAllowed by remember(topItem) {
            derivedStateOf {
                topItemPainterState is AsyncImagePainter.State.Success
            }
        }
        val isDislikeAllowed by remember(topItem) {
            derivedStateOf {
                topItemPainterState != null
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
                            DismissDirection.End.takeIf { isLikeAllowed }
                        ),
                    )
            )
        }

        FeedButtons(
            topItemState = topItemState,
            isLikeEnabled = isLikeAllowed,
            isDislikeEnabled = isDislikeAllowed,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun FeedButtons(
    topItemState: DismissibleState,
    isLikeEnabled: Boolean,
    isDislikeEnabled: Boolean,
    modifier: Modifier = Modifier,
) = Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
    val scope = rememberCoroutineScope()

    val dislikeButtonTargetScale by remember {
        derivedStateOf {
            getButtonScale(topItemState.horizontalDismissProgress * -1)
        }
    }
    val dislikeButtonScale by animateFloatAsState(dislikeButtonTargetScale)

    val likeButtonTargetScale by remember {
        derivedStateOf {
            getButtonScale(topItemState.horizontalDismissProgress)
        }
    }
    val likeButtonScale by animateFloatAsState(likeButtonTargetScale)

    // Dislike button
    FeedButton(
        onClick = {
            scope.launch {
                if (topItemState.dismissDirection != null) return@launch
                launch { topItemState.dismiss(DismissDirection.Start) }
            }
        },
        modifier = Modifier.graphicsLayer {
            scaleX = dislikeButtonScale
            scaleY = dislikeButtonScale
        },
        enabled = isDislikeEnabled
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
            scaleX = likeButtonScale
            scaleY = likeButtonScale
        },
        enabled = isLikeEnabled
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
        animationSpec = tween(600)
    )
    val contentAndOutlineColor by animateColorAsState(
        if (enabled) Color.White else Color.White.copy(alpha = 0.3f),
        animationSpec = tween(400)
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
        )
    ) {
        content()
    }
}

@Composable
private fun DislikeIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = stringResource(Res.string.nope),
        modifier = modifier.size(32.dp)
    )
}

@Composable
private fun LikeIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Rounded.Favorite,
        contentDescription = stringResource(Res.string.like),
        modifier = modifier.size(32.dp)
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

@Composable
expect fun SplashController(painterState: AsyncImagePainter.State)