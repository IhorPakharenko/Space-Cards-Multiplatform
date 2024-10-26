package com.isao.yfoo3.liked

import androidx.compose.runtime.Immutable
import com.isao.yfoo3.liked.model.LikedImageDisplayable

@Immutable
data class LikedUiState(
    val items: List<LikedImageDisplayable> = emptyList(),
    val shouldSortAscending: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)