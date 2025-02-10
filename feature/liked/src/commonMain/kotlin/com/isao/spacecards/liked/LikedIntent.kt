package com.isao.spacecards.liked

sealed class LikedIntent {
  data class SetSorting(val shouldSortAscending: Boolean) : LikedIntent()

  data class PageReached(val page: Int) : LikedIntent()

  data class ImageClicked(val item: PagedItem) : LikedIntent()

  data class AuthorClicked(val item: PagedItem) : LikedIntent()

  data class DeleteImageClicked(val item: PagedItem) : LikedIntent()
}
