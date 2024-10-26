package com.isao.yfoo3.core.data.mapper

import com.isao.yfoo3.core.db.model.LikedImageCached
import com.isao.yfoo3.core.model.LikedImage

fun LikedImageCached.toDomainModel() = LikedImage(
    id = id,
    imageId = imageId,
    source = source,
    dateAdded = dateAdded
)

fun LikedImage.toEntityModel() = LikedImageCached(
    id = id,
    imageId = imageId,
    source = source,
    dateAdded = dateAdded
)