package com.isao.yfoo3.presentation.liked.mapper

import com.isao.yfoo3.domain.model.LikedImage
import com.isao.yfoo3.presentation.liked.model.LikedImageDisplayable

fun LikedImage.toPresentationModel() = LikedImageDisplayable(
    id = id,
    imageUrl = source.getImageUrl(imageId),
    source = source
)