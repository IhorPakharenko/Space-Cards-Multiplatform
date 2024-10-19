package com.isao.yfoo3.presentation.feed.mapper

import com.isao.yfoo3.domain.model.FeedImage
import com.isao.yfoo3.presentation.feed.model.FeedItemDisplayable

fun FeedImage.toPresentationModel() = FeedItemDisplayable(
    id = id,
    imageId = imageId,
    source = source,
    imageUrl = source.getImageUrl(imageId),
    sourceUrl = source.websiteUrl
)