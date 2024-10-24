package com.isao.yfoo3.presentation.liked.model

import com.isao.yfoo3.data.model.ImageSource

data class LikedImageDisplayable(
    val id: String,
    val imageUrl: String,
    val source: ImageSource,
)