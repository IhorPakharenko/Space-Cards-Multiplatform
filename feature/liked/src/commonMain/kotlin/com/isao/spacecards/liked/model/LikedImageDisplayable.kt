package com.isao.spacecards.liked.model

import com.isao.spacecards.core.model.ImageSource

data class LikedImageDisplayable(
  val id: String,
  val imageUrl: String,
  val source: ImageSource,
)
