package com.isao.yfoo3.domain.model

import com.isao.yfoo3.data.model.ImageSource

data class FeedImage(
    val id: String,
    val imageId: String,
    val source: ImageSource
)