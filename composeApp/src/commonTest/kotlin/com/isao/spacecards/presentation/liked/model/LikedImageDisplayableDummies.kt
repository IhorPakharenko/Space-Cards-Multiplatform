package com.isao.spacecards.presentation.liked.model

import com.isao.spacecards.core.db.model.ImageSource
import com.isao.spacecards.liked.model.LikedImageDisplayable

object LikedImageDisplayableDummies {
    fun generateLikedImageDisplayables(size: Int) = List(size) { index ->
        LikedImageDisplayable(
            id = "$index",
            imageUrl = "https://example.com/${index}",
            source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST
        )
    }
}