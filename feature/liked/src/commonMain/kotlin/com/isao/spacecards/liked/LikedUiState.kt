package com.isao.spacecards.liked

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage

data class LikedUiState(
  val items: List<PagedItem> = emptyList(),
  val currentPage: Int = 0,
  val isLoading: Boolean = false,
  val shouldSortAscending: Boolean = false,
)

data class PagedItem(val page: Int, val astrobinImage: AstrobinImage)
