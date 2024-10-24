package com.isao.yfoo3.data.local.mapper

import com.isao.yfoo3.data.model.LikedImageCached
import com.isao.yfoo3.domain.model.LikedImage

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