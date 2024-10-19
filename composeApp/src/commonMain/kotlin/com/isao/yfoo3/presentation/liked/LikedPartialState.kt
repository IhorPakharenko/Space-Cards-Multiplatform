package com.isao.yfoo3.presentation.liked

import com.isao.yfoo3.presentation.liked.model.LikedImageDisplayable

sealed class LikedPartialState {

    data class Fetched(val items: List<LikedImageDisplayable>) : LikedPartialState()

    data class Error(val throwable: Throwable) : LikedPartialState()

    object Loading : LikedPartialState()

    data class Sorted(val shouldSortAscending: Boolean) : LikedPartialState()
}