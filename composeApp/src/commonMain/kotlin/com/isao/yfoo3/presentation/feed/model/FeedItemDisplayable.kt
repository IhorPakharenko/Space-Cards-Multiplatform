package com.isao.yfoo3.presentation.feed.model

import com.isao.yfoo3.domain.model.ImageSource

data class FeedItemDisplayable(
    val id: String,
    val imageId: String,
    val source: ImageSource,
    val imageUrl: String,
    val sourceUrl: String
)