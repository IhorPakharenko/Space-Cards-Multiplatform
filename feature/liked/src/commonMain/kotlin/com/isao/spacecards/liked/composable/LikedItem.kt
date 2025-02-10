package com.isao.spacecards.liked.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.isao.spacecards.feature.common.util.CatPreviewPlaceholder
import com.isao.spacecards.feature.common.util.debugPlaceholder
import com.isao.spacecards.liked.PagedItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LikedItem(
  item: PagedItem,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (LocalInspectionMode.current) {
    CatPreviewPlaceholder()
    return
  }
  val painter = rememberAsyncImagePainter(
    model = item.astrobinImage.urlHd,
    placeholder = debugPlaceholder(Color.Magenta),
    contentScale = ContentScale.Crop,
  )
  if (painter.state.collectAsState().value !is AsyncImagePainter.State.Error) {
    Image(
      painter = painter,
      contentDescription = null,
      modifier = modifier
        .combinedClickable(
          onClick = onClick,
          onLongClick = onLongClick,
        ),
      contentScale = ContentScale.Crop,
    )
  } else {
    Box(
      modifier = modifier
        .combinedClickable(
          onClick = onClick,
          onLongClick = onLongClick,
        ),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Default.ErrorOutline,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(0.5f),
        tint = MaterialTheme.colorScheme.error,
      )
    }
  }
}
