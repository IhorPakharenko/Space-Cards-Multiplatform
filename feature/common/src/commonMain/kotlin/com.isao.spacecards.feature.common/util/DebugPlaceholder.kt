package com.isao.spacecards.feature.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun debugPlaceholder(debugPreview: DrawableResource) = if (LocalInspectionMode.current) {
  painterResource(debugPreview)
} else {
  null
}

@Composable
fun debugPlaceholder(color: Color) = if (LocalInspectionMode.current) {
  ColorPainter(color)
} else {
  null
}
