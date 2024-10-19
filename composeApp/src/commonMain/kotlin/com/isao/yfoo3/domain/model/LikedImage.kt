package com.isao.yfoo3.domain.model

import kotlinx.datetime.Instant

data class LikedImage(
    val id: String,
    val imageId: String,
    val source: ImageSource,
    val dateAdded: Instant
)