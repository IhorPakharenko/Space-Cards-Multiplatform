package com.isao.spacecards.liked

import com.isao.spacecards.liked.model.LikedImageDisplayable

sealed class LikedPartialState {
  data class Fetched(val items: List<LikedImageDisplayable>) : LikedPartialState()

  data class Error(val throwable: Throwable) : LikedPartialState()

  data object Loading : LikedPartialState()

  data class Sorted(val shouldSortAscending: Boolean) : LikedPartialState()
}
