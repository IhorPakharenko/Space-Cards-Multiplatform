@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.isao.spacecards.core.designsystem

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.isao.spacecards.feature.common.extension.findActivity

@Composable
actual fun getWindowSizeClass(): WindowSizeClass {
  // Must not be called outside the context of activity
  return calculateWindowSizeClass(LocalContext.current.findActivity()!!)
}
