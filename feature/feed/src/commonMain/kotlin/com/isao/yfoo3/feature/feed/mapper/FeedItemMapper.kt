package com.isao.yfoo3.feature.feed.mapper

import com.isao.yfoo3.core.model.FeedImage
import com.isao.yfoo3.feature.feed.model.FeedItemDisplayable

fun FeedImage.toPresentationModel() = FeedItemDisplayable(
  id = id,
  imageId = imageId,
  source = source,
  imageUrl = source.getImageUrl(imageId),
  sourceUrl = source.websiteUrl,
)
