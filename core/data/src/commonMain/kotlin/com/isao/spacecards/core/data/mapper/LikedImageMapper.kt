package com.isao.spacecards.core.data.mapper

import com.isao.spacecards.core.db.model.LikedImageCached
import com.isao.spacecards.core.model.LikedImage

fun LikedImageCached.toDomainModel() = LikedImage(
  id = id,
  imageId = imageId,
  source = source,
  dateAdded = dateAdded,
)

fun LikedImage.toEntityModel() = LikedImageCached(
  id = id,
  imageId = imageId,
  source = source,
  dateAdded = dateAdded,
)
