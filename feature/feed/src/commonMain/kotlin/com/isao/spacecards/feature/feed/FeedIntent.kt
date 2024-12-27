package com.isao.spacecards.feature.feed

import com.isao.spacecards.feature.feed.model.FeedItemDisplayable

sealed class FeedIntent {
  data class Like(val item: FeedItemDisplayable) : FeedIntent()

  data class Dislike(val item: FeedItemDisplayable) : FeedIntent()
}
