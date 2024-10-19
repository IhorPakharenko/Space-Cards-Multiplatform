package com.isao.yfoo3.presentation.liked

import com.isao.yfoo3.presentation.liked.model.LikedImageDisplayable

sealed class LikedIntent {

    data class SetSorting(val shouldSortAscending: Boolean) : LikedIntent()

    data class ImageClicked(val item: LikedImageDisplayable) : LikedIntent()

    data class ViewImageSourceClicked(val item: LikedImageDisplayable) : LikedIntent()

    data class DeleteImageClicked(val item: LikedImageDisplayable) : LikedIntent()
}