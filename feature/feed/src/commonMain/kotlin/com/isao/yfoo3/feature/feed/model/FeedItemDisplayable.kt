package com.isao.yfoo3.feature.feed.model

import com.isao.yfoo3.core.model.ImageSource

data class FeedItemDisplayable(
  val id: String,
  val imageId: String,
  val source: ImageSource,
  val imageUrl: String,
  val sourceUrl: String,
)
