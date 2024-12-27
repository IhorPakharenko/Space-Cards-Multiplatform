package com.isao.spacecards.core.db.model

import com.isao.spacecards.core.model.ImageSource
import kotlinx.datetime.Instant

data class LikedImageCached(
  val id: String,
  val imageId: String,
  val source: ImageSource,
  val dateAdded: Instant,
)
