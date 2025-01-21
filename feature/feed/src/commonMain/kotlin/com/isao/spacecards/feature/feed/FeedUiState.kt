package com.isao.spacecards.feature.feed

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Instant

@Stable // Mark as stable manually due to Flow.
// TODO consider adding Flow<PagingData<AstrobinImage>> into exclusions
data class FeedUiState(
  val items: Flow<PagingData<AstrobinImage>> = emptyFlow(),
  val startFromInstant: Instant? = null,
)
