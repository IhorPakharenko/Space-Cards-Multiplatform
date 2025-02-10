package com.isao.spacecards.feature.feed.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage

@Composable
internal fun FeedCard(
  item: AstrobinImage,
  painter: AsyncImagePainter,
  modifier: Modifier = Modifier,
) {
  Card(modifier) {
    Box {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
      )
      Column(
        Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(
                Color.Black.copy(alpha = 0.5f),
                Color.Black.copy(alpha = 0f),
              ),
            ),
          ).padding(horizontal = 8.dp),
      ) {
        Text(
          text = item.title ?: "",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3,
        )
        Text(
          text = item.user,
          style = MaterialTheme.typography.titleSmall,
          color = Color.White,
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2,
        )
      }
    }
  }
}

@Composable
internal fun LoadingCard(modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      CircularProgressIndicator()
    }
  }
}

@Composable
internal fun ErrorCard(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.onErrorContainer,
    LocalTextStyle provides MaterialTheme.typography.headlineSmall,
  ) {
    Card(
      modifier = modifier,
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
      content = content,
    )
  }
}
