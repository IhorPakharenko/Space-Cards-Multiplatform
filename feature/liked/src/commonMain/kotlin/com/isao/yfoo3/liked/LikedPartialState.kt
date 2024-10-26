package com.isao.yfoo3.liked

import com.isao.yfoo3.liked.model.LikedImageDisplayable

sealed class LikedPartialState {

    data class Fetched(val items: List<LikedImageDisplayable>) : LikedPartialState()

    data class Error(val throwable: Throwable) : LikedPartialState()

    data object Loading : LikedPartialState()

    data class Sorted(val shouldSortAscending: Boolean) : LikedPartialState()
}