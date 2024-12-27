package com.isao.spacecards.feature.feed.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.isao.spacecards.feature.feed.model.FeedItemDisplayable

@Composable
fun PreloadFeedItem(
  item: FeedItemDisplayable?,
  width: Dp,
  height: Dp,
) {
  val context = LocalPlatformContext.current
  val widthPx = with(LocalDensity.current) { width.roundToPx() }
  val heightPx = with(LocalDensity.current) { height.roundToPx() }

  LaunchedEffect(item) {
    if (item == null) return@LaunchedEffect
    SingletonImageLoader.get(context).enqueue(
      ImageRequest
        .Builder(context)
        .data(item.imageUrl)
        .size(
          widthPx,
          heightPx,
        )
//                .transformations(BitmapTransformations.getFor(item.source))
        .build(),
    )
  }
}
