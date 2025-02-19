package com.isao.spacecards.component.astrobinimages.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.raise.either
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntityQueries
import com.isao.spacecards.component.astrobinimages.db.toDomainModel
import com.isao.spacecards.component.astrobinimages.db.toEntityModel
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.network.AstrobinImageApi
import com.isao.spacecards.component.astrobinimages.network.toDomainModel
import com.isao.spacecards.foundation.ApiFailure
import isao.pager.Config
import isao.pager.RemoteSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

//TODO limit the amount of viewed images stored in local db
//TODO use queries.selectLastViewed to enable restoring the last dismissed image
internal class DefaultAstrobinImageRepository(
  private val queries: AstrobinImageEntityQueries,
  private val api: AstrobinImageApi,
) : AstrobinImageRepository {
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

  override suspend fun setBookmarkedAndViewed(
    id: Int,
    at: Instant?,
  ) = withContext(Dispatchers.IO) {
    queries.transaction {
      queries.setBookmarkedAt(id = id, bookmarkedAt = at)
      queries.setViewedAt(id = id, viewedAt = at)
    }
  }

  override suspend fun resetViewedForAll() = withContext(Dispatchers.IO) {
    queries.resetViewedAtForAll()
  }

  //TODO flow update is triggered for each page even if the
  // result does not change. Can we do something about it?
  override fun observePage(
    key: Instant?,
    config: Config<Instant?>,
  ): Flow<List<AstrobinImage>> = queries
    .selectByUploadedAt(
      isBookmarked = null,
      isViewed = false,
      uploadedEarlierThan = key,
      limit = config.pageSize.toLong(),
      offset = 0,
    ).asFlow()
    .mapToList(Dispatchers.IO)
    .map { page ->
      page.map { it.toDomainModel() }
    }.flowOn(Dispatchers.IO)

  override fun observeBookmarkedPage(
    key: Int,
    config: Config<Int>,
    shouldSortAscending: Boolean,
  ) = queries
    .selectByBookmarkedAt(
      isBookmarked = true,
      isViewed = null,
      shouldSortAscending = if (shouldSortAscending) 1L else 0L,
      limit = config.pageSize.toLong(),
      offset = config.pageSize * key.toLong(),
    ).asFlow()
    .mapToList(Dispatchers.IO)
    .map { page ->
      page.map { it.toDomainModel() }
    }.flowOn(Dispatchers.IO)

  override suspend fun fetchPage(
    key: Instant?,
    config: Config<Instant?>,
  ): Either<ApiFailure, RemoteSuccess> = withContext(Dispatchers.IO) {
    //TODO if the page is partially cached, fetch only the missing part instead of the whole page
    either {
      val images = api
        .getImages(
          uploadedEarlierThan = key,
          limit = config.pageSize,
          offset = 0,
        ).bind()

      // Insert a new entity. If already exists, update only properties
      // that can be fetched and do not overwrite viewedAt and bookmarkedAt
      queries.transaction {
        images.objects.forEach {
          val entity = it.toDomainModel().toEntityModel()
          queries.insertEntityOrIgnore(entity)
          if (queries.selectLastInsertRowId().executeAsOne() != entity.id.toLong()) {
            queries.update(
              bookmarks = entity.bookmarks,
              comments = entity.comments,
              dataSource = entity.dataSource,
              description = entity.description,
              license = entity.license,
              licenseName = entity.licenseName,
              likes = entity.likes,
              publishedAt = entity.publishedAt,
              title = entity.title,
              updatedAt = entity.updatedAt,
              uploadedAt = entity.uploadedAt,
              urlGallery = entity.urlGallery,
              urlHd = entity.urlHd,
              urlHistogram = entity.urlHistogram,
              urlReal = entity.urlReal,
              urlRegular = entity.urlRegular,
              urlThumb = entity.urlThumb,
              user = entity.user,
              views = entity.views,
              id = entity.id,
            )
          }
        }
      }

      return@either RemoteSuccess(
        isLastPage = images.objects.size < config.pageSize,
      )
    }
  }
}
