package com.isao.yfoo3.core.common.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.isao.yfoo3.resources.Res
import com.isao.yfoo3.resources.placeholder_cat
import org.jetbrains.compose.resources.painterResource

@Composable
fun CatPreviewPlaceholder(modifier: Modifier = Modifier) = Image(
    painter = painterResource(Res.drawable.placeholder_cat),
    contentDescription = null,
    modifier = modifier,
    contentScale = ContentScale.Crop
)