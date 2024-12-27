package com.isao.yfoo3.feature.feed

import com.isao.yfoo3.feature.feed.model.FeedItemDisplayable

sealed class FeedIntent {
  data class Like(val item: FeedItemDisplayable) : FeedIntent()

  data class Dislike(val item: FeedItemDisplayable) : FeedIntent()
}
