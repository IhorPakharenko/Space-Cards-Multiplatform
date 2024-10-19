package com.isao.yfoo3.core.utils

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import yfoomultiplatform.composeapp.generated.resources.Res
import yfoomultiplatform.composeapp.generated.resources.placeholder_cat

@Composable
fun CatPreviewPlaceholder(modifier: Modifier = Modifier) = Image(
    painter = painterResource(Res.drawable.placeholder_cat),
    contentDescription = null,
    modifier = modifier,
    contentScale = ContentScale.Crop
)