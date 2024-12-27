package com.isao.spacecards.feature.feed.mapper

import com.isao.spacecards.core.model.FeedImage
import com.isao.spacecards.feature.feed.model.FeedItemDisplayable

fun FeedImage.toPresentationModel() = FeedItemDisplayable(
  id = id,
  imageId = imageId,
  source = source,
  imageUrl = source.getImageUrl(imageId),
  sourceUrl = source.websiteUrl,
)
