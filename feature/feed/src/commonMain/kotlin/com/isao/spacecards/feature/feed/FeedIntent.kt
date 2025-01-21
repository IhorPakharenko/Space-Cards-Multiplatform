package com.isao.spacecards.feature.feed

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import kotlinx.datetime.Instant

sealed class FeedIntent {
  data class StartFromInstant(val instant: Instant?) : FeedIntent()

  data class Like(val item: AstrobinImage) : FeedIntent()

  data class Dislike(val item: AstrobinImage) : FeedIntent()
}
