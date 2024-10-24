package com.isao.yfoo3.data.model

import kotlinx.datetime.Instant

data class LikedImageCached(
    val id: String,
    val imageId: String,
    val source: ImageSource,
    val dateAdded: Instant
)