package com.isao.spacecards.feature.feed.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.Disposable
import coil3.request.ImageRequest

@Composable
fun PreloadImages(urls: List<String>) {
  val requestedUrls = remember { mutableStateMapOf<String, Disposable>() }
  val context = LocalPlatformContext.current

  LaunchedEffect(urls) {
    urls.forEach { url ->
      if (url !in requestedUrls) {
        requestedUrls[url] = ImageLoader(context).enqueue(
          ImageRequest
            .Builder(context)
            .data(url)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .build(),
        )
      }
    }

    // Cancel requests for URLs that are no longer in the list
    val currentUrls = urls.toSet()
    requestedUrls.keys.toList().forEach { url ->
      if (url !in currentUrls) {
        requestedUrls.remove(url)?.dispose()
      }
    }
  }

  // Cancel all pending requests when PreloadImages leaves composition
  DisposableEffect(Unit) {
    onDispose {
      requestedUrls.values.forEach { it.dispose() }
      requestedUrls.clear()
    }
  }
}
