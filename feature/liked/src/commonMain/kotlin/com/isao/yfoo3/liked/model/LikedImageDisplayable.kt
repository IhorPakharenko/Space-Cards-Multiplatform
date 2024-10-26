package com.isao.yfoo3.liked.model

import com.isao.yfoo3.core.model.ImageSource

data class LikedImageDisplayable(
    val id: String,
    val imageUrl: String,
    val source: ImageSource,
)