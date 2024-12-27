@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.isao.yfoo3.core.designsystem

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.isao.yfoo3.core.common.extension.findActivity

@Composable
actual fun getWindowSizeClass(): WindowSizeClass {
  // Must not be called outside the context of activity
  return calculateWindowSizeClass(LocalContext.current.findActivity()!!)
}
