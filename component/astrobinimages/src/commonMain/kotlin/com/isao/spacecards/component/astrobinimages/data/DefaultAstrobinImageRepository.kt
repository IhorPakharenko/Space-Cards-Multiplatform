package com.isao.spacecards.component.astrobinimages.data

import androidx.paging.PagingSource
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntityQueries
import com.isao.spacecards.component.astrobinimages.db.AstrobinPagingSource
import com.isao.spacecards.component.astrobinimages.db.model.toDomainModel
import com.isao.spacecards.component.astrobinimages.db.model.toEntityModel
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.common.extension.mapValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

internal class DefaultAstrobinImageRepository(private val queries: AstrobinImageEntityQueries) :
  AstrobinImageRepository {
  override fun getPagingSource(
    startFromInstantExclusive: Instant?,
  ): PagingSource<Instant, AstrobinImage> =
    AstrobinPagingSource(queries, startFromInstantExclusive).mapValue(
      toNew = { it.toDomainModel() },
      toOld = { it.toEntityModel() },
    )

  override suspend fun setViewed(
    id: Int,
    at: Instant?,
  ) = withContext(Dispatchers.IO) {
    queries.setViewedAt(id = id, viewedAt = at)
  }

  override suspend fun setBookmarked(
    id: Int,
    at: Instant?,
  ) = withContext(Dispatchers.IO) {
    queries.setBookmarkedAt(id = id, bookmarkedAt = at)
  }
}
