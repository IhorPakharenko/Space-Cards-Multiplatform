package com.isao.spacecards.component.astrobinimages.domain

import arrow.core.Either
import com.isao.spacecards.foundation.ApiFailure
import isao.pager.Config
import isao.pager.RemoteSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface AstrobinImageRepository {
  suspend fun setViewed(
    id: Int,
    at: Instant?,
  )

  suspend fun setBookmarked(
    id: Int,
    at: Instant?,
  )

  suspend fun setBookmarkedAndViewed(
    id: Int,
    at: Instant?,
  )

  suspend fun resetViewedForAll()

  fun observePage(
    key: Instant?,
    config: Config<Instant?>,
  ): Flow<List<AstrobinImage>>

  suspend fun fetchPage(
    key: Instant?,
    config: Config<Instant?>,
  ): Either<ApiFailure, RemoteSuccess>

  fun observeBookmarkedPage(
    key: Int,
    config: Config<Int>,
    shouldSortAscending: Boolean,
  ): Flow<List<AstrobinImage>>
}
