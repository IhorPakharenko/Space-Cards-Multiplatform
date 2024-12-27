package com.isao.spacecards.core.model

import kotlinx.datetime.Instant

data class LikedImage(
  val id: String,
  val imageId: String,
  val source: ImageSource,
  val dateAdded: Instant,
)
