package com.isao.yfoo3.liked.mapper

import com.isao.yfoo3.core.model.LikedImage
import com.isao.yfoo3.liked.model.LikedImageDisplayable

fun LikedImage.toPresentationModel() = LikedImageDisplayable(
  id = id,
  imageUrl = source.getImageUrl(imageId),
  source = source,
)
