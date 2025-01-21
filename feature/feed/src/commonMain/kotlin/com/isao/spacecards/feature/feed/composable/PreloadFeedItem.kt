package com.isao.spacecards.feature.feed.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import co.touchlab.kermit.Logger
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

//TODO ugly. Improve.
@Composable
fun PreloadImages(
  items: Set<String>,
  width: Dp,
  height: Dp,
) {
  val logger = remember { Logger.withTag("PreloadImages") }

  val context = LocalPlatformContext.current
  val widthPx = with(LocalDensity.current) { width.roundToPx() }
  val heightPx = with(LocalDensity.current) { height.roundToPx() }

  var unscheduledRequests by remember { mutableStateOf<Set<String>>(emptySet()) }
  var scheduledRequests by remember { mutableStateOf<Set<String>>(emptySet()) }
  var finishedRequests by remember { mutableStateOf<Set<String>>(emptySet()) }

  LaunchedEffect(items) {
    unscheduledRequests = items
  }

  LaunchedEffect(Unit) {
    val unscheduledRequestsFlow = snapshotFlow { unscheduledRequests }.stateIn(this)
    val scheduledRequestsFlow = snapshotFlow { scheduledRequests }.stateIn(this)
    val finishedRequestsFlow = snapshotFlow { finishedRequests }.stateIn(this)

    combine(
      unscheduledRequestsFlow,
      scheduledRequestsFlow,
      finishedRequestsFlow,
    ) { unscheduled, scheduled, finished ->
      if (scheduled.isNotEmpty()) {
        logger.i {
          "Launched requests is not empty, skipping"
        }
        return@combine
      }
      val shouldPrefetch = unscheduled.filterNot { it in finished }.take(4).toSet()
      logger.i {
        "Should prefetch ${shouldPrefetch.size} requests: ${shouldPrefetch.joinToString()}"
      }
//      if (scheduled.containsAll(shouldPrefetch)) return@combine
      scheduledRequests = shouldPrefetch
      logger.i {
        "Sending to prefetch ${shouldPrefetch.size} requests: ${shouldPrefetch.joinToString()}"
      }
    }.collect()
  }

  val scope = rememberCoroutineScope()
  val activeJobs = remember { mutableMapOf<String, Deferred<*>>() }

  LaunchedEffect(scheduledRequests) {
    // Cancel jobs for requests no longer in launchedRequests
    val obsoleteKeys = activeJobs.keys - scheduledRequests
    obsoleteKeys.forEach { key ->
      activeJobs[key]?.cancel()
      activeJobs.remove(key)
    }

    // Launch new jobs for newly added requests
    val newRequests = scheduledRequests - activeJobs.keys
    newRequests.forEach { request ->
      activeJobs[request] = scope.async {
        try {
          logger.i { "Launching request: $request" }
          preloadImage(context, request, widthPx, heightPx)
          logger.i { "Finished request: $request" }
        } finally {
          // Remove the finished request from activeJobs
          //TODO race conditioooons
          finishedRequests = finishedRequests + request
          scheduledRequests = scheduledRequests - request
          activeJobs.remove(request)
        }
      }
    }

    logger.i { "Active requests count: ${activeJobs.size}" }
    logger.i { "Finished requests count: ${finishedRequests.size}" }
  }
}

private suspend fun preloadImage(
  context: PlatformContext,
  url: String,
  widthPx: Int,
  heightPx: Int,
) = SingletonImageLoader.get(context).execute(
  ImageRequest
    .Builder(context)
    .data(url)
    .size(
      widthPx,
      heightPx,
    ).build(),
)
