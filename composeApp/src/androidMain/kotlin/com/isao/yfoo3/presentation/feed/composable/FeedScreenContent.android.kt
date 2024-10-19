package com.isao.yfoo3.presentation.feed.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImagePainter
import com.isao.yfoo3.core.extensions.findActivity
import com.isao.yfoo3.core.utils.SplashScreenHost
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.timeout
import kotlin.time.Duration.Companion.seconds

/**
 * Keeps splash screen on screen for an extra second or until the first image is prepared.
 * Does nothing if splash screen is not allowed to be drawn longer than necessary
 * ([SplashScreenHost.shouldKeepSplashScreen] is false)
 */
@OptIn(FlowPreview::class)
@Composable
actual fun SplashController(painterState: AsyncImagePainter.State) {
    val splashScreenHost = LocalContext.current.findActivity() as? SplashScreenHost
    if (splashScreenHost?.shouldKeepSplashScreen != true) return
    LaunchedEffect(Unit) {
        snapshotFlow { painterState }
            .map {
                it is AsyncImagePainter.State.Success
                        || it is AsyncImagePainter.State.Error
            }
            .timeout(1.seconds)
            .catch { emit(true) }
            .filter { it }
            .collect {
                splashScreenHost.shouldKeepSplashScreen = false
            }
    }
}