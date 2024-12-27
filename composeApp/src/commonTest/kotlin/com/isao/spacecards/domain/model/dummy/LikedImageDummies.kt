package com.isao.spacecards.domain.model.dummy

import com.isao.spacecards.core.db.model.ImageSource
import com.isao.spacecards.domain.model.LikedImage
import kotlinx.datetime.Instant

object LikedImageDummies {
    val LikedImage1 = LikedImage(
        id = "olderImg",
        imageId = "1",
        source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST,
        dateAdded = Instant.parse("2020-01-01T00:00:00Z")
    )
    val LikedImage2 = LikedImage(
        id = "newerImg",
        imageId = "2",
        source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST,
        dateAdded = Instant.parse("2020-01-01T00:01:00Z")
    )
}