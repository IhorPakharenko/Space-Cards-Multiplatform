package com.isao.spacecards.core.data.mapper

import com.isao.spacecards.core.db.model.FeedImageCached
import com.isao.spacecards.core.model.FeedImage
import com.isao.spacecards.core.model.LikedImage
import kotlinx.datetime.Clock

fun FeedImageCached.toDomainModel() = FeedImage(
  id = id,
  imageId = imageId,
  source = source,
)

fun FeedImage.toEntityModel() = FeedImageCached(
  id = id,
  imageId = imageId,
  source = source,
)

fun FeedImage.toLikedImage() = LikedImage(
  id = id,
  imageId = imageId,
  source = source,
  dateAdded = Clock.System.now(),
)
