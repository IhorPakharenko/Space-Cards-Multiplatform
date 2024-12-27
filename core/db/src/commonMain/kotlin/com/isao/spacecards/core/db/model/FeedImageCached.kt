package com.isao.spacecards.core.db.model

import com.isao.spacecards.core.model.ImageSource

data class FeedImageCached(
  val id: String,
  val imageId: String,
  val source: ImageSource,
)
