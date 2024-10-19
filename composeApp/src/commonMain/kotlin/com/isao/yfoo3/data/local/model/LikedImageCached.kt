package com.isao.yfoo3.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.isao.yfoo3.domain.model.ImageSource
import kotlinx.datetime.Instant

@Entity
data class LikedImageCached(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "imageId")
    val imageId: String,
    @ColumnInfo(name = "source")
    val source: ImageSource,
    @ColumnInfo(name = "dateAdded")
    val dateAdded: Instant
)