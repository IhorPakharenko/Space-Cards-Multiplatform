package com.isao.spacecards.core.db.model

import kotlinx.datetime.Instant

data class LikedImageCached(
  val id: String,
  val imageId: String,
  val source: String,
  val dateAdded: Instant,
)
