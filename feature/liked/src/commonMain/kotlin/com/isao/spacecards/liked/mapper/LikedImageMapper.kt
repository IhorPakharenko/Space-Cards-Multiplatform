package com.isao.spacecards.liked.mapper

import com.isao.spacecards.core.model.LikedImage
import com.isao.spacecards.liked.model.LikedImageDisplayable

fun LikedImage.toPresentationModel() = LikedImageDisplayable(
  id = id,
  imageUrl = source.getImageUrl(imageId),
  source = source,
)
