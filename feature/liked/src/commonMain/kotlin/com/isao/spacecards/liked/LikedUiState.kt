package com.isao.spacecards.liked

import androidx.compose.runtime.Immutable
import com.isao.spacecards.liked.model.LikedImageDisplayable

@Immutable
data class LikedUiState(
  val items: List<LikedImageDisplayable> = emptyList(),
  val shouldSortAscending: Boolean = false,
  val isLoading: Boolean = false,
  val isError: Boolean = false,
)
