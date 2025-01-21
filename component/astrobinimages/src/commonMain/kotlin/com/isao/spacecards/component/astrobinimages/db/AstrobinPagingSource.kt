package com.isao.spacecards.component.astrobinimages.db

import androidx.paging.PagingState
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntity
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntityQueries
import com.isao.spacecards.component.common.QueryPagingSource
import kotlinx.datetime.Instant

internal class AstrobinPagingSource(
  private val queries: AstrobinImageEntityQueries,
  private val startFromInstantExclusive: Instant? = null,
) : QueryPagingSource<Instant, AstrobinImageEntity>() {
  override fun getRefreshKey(state: PagingState<Instant, AstrobinImageEntity>): Instant? = null

  override suspend fun load(params: LoadParams<Instant>): LoadResult<Instant, AstrobinImageEntity> {
    val images = queries
      .select(
        isBookmarked = false, isViewed = false,
        uploadedEarlierThan = params.key ?: startFromInstantExclusive,
        limit = params.loadSize.toLong(),
        offset = 0,
      ).also { currentQuery = it }
      .executeAsList()
    return LoadResult.Page(
      data = images,
      prevKey = null,
      nextKey = images.lastOrNull()?.uploaded,
    )
  }
}
