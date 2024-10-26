package com.isao.yfoo3.core.db.model

import com.isao.yfoo3.core.model.ImageSource

data class FeedImageCached(
    val id: String,
    val imageId: String,
    val source: ImageSource
)