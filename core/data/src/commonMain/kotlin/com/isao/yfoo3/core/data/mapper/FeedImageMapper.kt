package com.isao.yfoo3.core.data.mapper

import com.isao.yfoo3.core.db.model.FeedImageCached
import com.isao.yfoo3.core.model.FeedImage
import com.isao.yfoo3.core.model.LikedImage
import kotlinx.datetime.Clock

fun FeedImageCached.toDomainModel() = FeedImage(
    id = id,
    imageId = imageId,
    source = source
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
    dateAdded = Clock.System.now()
)