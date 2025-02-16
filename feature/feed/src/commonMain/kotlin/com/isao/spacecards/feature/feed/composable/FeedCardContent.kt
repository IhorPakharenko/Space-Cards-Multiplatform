package com.isao.spacecards.feature.feed.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.foundation.ApiFailure
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.error_connection
import com.isao.spacecards.resources.error_image
import com.isao.spacecards.resources.error_server_astrobin
import com.isao.spacecards.resources.retry
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FeedCardContent(
  item: AstrobinImage,
  painter: AsyncImagePainter,
  modifier: Modifier = Modifier,
) {
  Box(modifier) {
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
        ).padding(horizontal = 8.dp)
        .padding(top = 4.dp),
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

@Composable
internal fun LoadingCardContent(modifier: Modifier = Modifier) {
  Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
}

@Composable
internal fun ErrorCardContent(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  Box(
    modifier = modifier.background(MaterialTheme.colorScheme.errorContainer),
  ) {
    CompositionLocalProvider(
      LocalContentColor provides MaterialTheme.colorScheme.onErrorContainer,
      LocalTextStyle provides MaterialTheme.typography.headlineSmall,
    ) {
      content()
    }
  }
}

@Composable
internal fun ApiFailureCardContent(
  failure: ApiFailure,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) = ErrorCardContent(modifier) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize().padding(16.dp),
  ) {
    Text(
      text = stringResource(
        when (failure) {
          ApiFailure.Connection -> Res.string.error_connection
          is ApiFailure.Serialization, is ApiFailure.Server ->
            Res.string.error_server_astrobin
        },
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(bottom = 16.dp),
    )
    Button(onClick = { onRetry() }) {
      Text(stringResource(Res.string.retry))
    }
  }
}

@Composable
internal fun CoilErrorCardContent(
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) = ErrorCardContent(modifier) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize().padding(16.dp),
  ) {
    Text(
      // TODO check which exceptions are thrown by Coil
      text = stringResource(Res.string.error_image),
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(bottom = 16.dp),
    )
    Button(onClick = { onRetry() }) {
      Text(stringResource(Res.string.retry))
    }
  }
}
