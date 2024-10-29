@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.isao.yfoo3.core.designsystem

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@Composable
actual fun getWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass()
}