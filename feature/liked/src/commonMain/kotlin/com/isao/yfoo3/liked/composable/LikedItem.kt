package com.isao.yfoo3.liked.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.isao.yfoo3.core.common.util.CatPreviewPlaceholder
import com.isao.yfoo3.core.common.util.debugPlaceholder
import com.isao.yfoo3.liked.model.LikedImageDisplayable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikedItem(
    item: LikedImageDisplayable,
    width: Dp,
    height: Dp,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        CatPreviewPlaceholder()
        return
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(item.imageUrl)
            // The size has to be provided since we rely on AsyncImagePager.state for the placeholder
            // https://coil-kt.github.io/coil/compose/#observing-asyncimagepainterstate
            .size(
                with(LocalDensity.current) { width.roundToPx() },
                with(LocalDensity.current) { height.roundToPx() }
            )
//            .transformations(BitmapTransformations.getFor(item.source))
            .build(),
        placeholder = debugPlaceholder(Color.Magenta),
        contentScale = ContentScale.Crop,
    )
    if (painter.state.collectAsState().value !is AsyncImagePainter.State.Error) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
//                .placeholder(
//                    visible = painter.state is AsyncImagePainter.State.Loading,
//                    highlight = PlaceholderHighlight.shimmer()
//                )
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(
            modifier = modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.5f),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}