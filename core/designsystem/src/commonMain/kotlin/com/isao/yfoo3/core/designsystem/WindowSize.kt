package com.isao.yfoo3.core.designsystem

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

@Composable
expect fun getWindowSizeClass(): WindowSizeClass

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    error("LocalWindowSizeClass must be provided as early as possible")
}
