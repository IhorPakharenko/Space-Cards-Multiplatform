package com.isao.yfoo3.core.db.model

import com.isao.yfoo3.core.model.ImageSource
import kotlinx.datetime.Instant

data class LikedImageCached(
    val id: String,
    val imageId: String,
    val source: ImageSource,
    val dateAdded: Instant
)