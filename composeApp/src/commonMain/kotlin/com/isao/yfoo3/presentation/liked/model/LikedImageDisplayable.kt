package com.isao.yfoo3.presentation.liked.model

import com.isao.yfoo3.domain.model.ImageSource

data class LikedImageDisplayable(
    val id: String,
    val imageUrl: String,
    val source: ImageSource,
)