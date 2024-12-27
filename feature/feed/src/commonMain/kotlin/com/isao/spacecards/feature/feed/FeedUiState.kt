package com.isao.spacecards.feature.feed

import androidx.compose.runtime.Immutable
import com.isao.spacecards.feature.feed.model.FeedItemDisplayable

@Immutable
data class FeedUiState(
  val items: List<FeedItemDisplayable> = emptyList(),
  val isLoading: Boolean = false,
  val isError: Boolean = false,
)
