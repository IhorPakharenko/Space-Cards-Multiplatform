package com.isao.spacecards.component.astrobinimages.domain

import androidx.paging.PagingSource
import kotlinx.datetime.Instant

interface AstrobinImageRepository {
  fun getPagingSource(
    startFromInstantExclusive: Instant? = null,
  ): PagingSource<Instant, AstrobinImage>

  suspend fun setViewed(
    id: Int,
    at: Instant?,
  )

  suspend fun setBookmarked(
    id: Int,
    at: Instant?,
  )
}
