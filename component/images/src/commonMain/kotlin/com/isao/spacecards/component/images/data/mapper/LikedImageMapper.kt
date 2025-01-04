package com.isao.spacecards.component.images.data.mapper

import com.isao.spacecards.component.images.domain.model.ImageSource
import com.isao.spacecards.component.images.domain.model.LikedImage
import com.isao.spacecards.core.db.model.LikedImageCached

fun LikedImageCached.toDomainModel() = LikedImage(
  id = id,
  imageId = imageId,
  source = ImageSource.valueOf(source),
  dateAdded = dateAdded,
)

fun LikedImage.toEntityModel() = LikedImageCached(
  id = id,
  imageId = imageId,
  source = source.name,
  dateAdded = dateAdded,
)
