package com.isao.spacecards.component.images.data.mapper

import com.isao.spacecards.component.images.domain.model.FeedImage
import com.isao.spacecards.component.images.domain.model.ImageSource
import com.isao.spacecards.core.db.model.FeedImageCached

internal fun FeedImageCached.toDomainModel() = FeedImage(
  id = id,
  imageId = imageId,
  source = ImageSource.valueOf(source),
)

internal fun FeedImage.toEntityModel() = FeedImageCached(
  id = id,
  imageId = imageId,
  source = source.name,
)
