package com.isao.spacecards.feature.feed

sealed class FeedIntent {
  data class StartFromMillisUTC(val millis: Long) : FeedIntent()

  data object Retry : FeedIntent()

  class Like(val item: PagedItem) : FeedIntent()

  data class Dislike(val item: PagedItem) : FeedIntent()
}
