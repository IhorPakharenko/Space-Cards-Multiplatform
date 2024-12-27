package com.isao.spacecards.feature.feed.model

import com.isao.spacecards.core.model.ImageSource

data class FeedItemDisplayable(
  val id: String,
  val imageId: String,
  val source: ImageSource,
  val imageUrl: String,
  val sourceUrl: String,
)
