package com.isao.spacecards.feature.feed

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.foundation.ApiFailure
import kotlinx.datetime.Instant

data class FeedUiState(
  val items: List<PagedItem> = emptyList(),
  val startFromInstant: Instant? = null,
)

data class PagedItem(
  val page: Instant?,
  val lastValidData: AstrobinImage?,
  val isLoading: Boolean,
  val remoteFailure: ApiFailure?,
)
