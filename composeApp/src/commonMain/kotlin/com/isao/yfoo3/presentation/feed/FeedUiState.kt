package com.isao.yfoo3.presentation.feed

import androidx.compose.runtime.Immutable
import com.isao.yfoo3.presentation.feed.model.FeedItemDisplayable

@Immutable
data class FeedUiState(
    val items: List<FeedItemDisplayable> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)