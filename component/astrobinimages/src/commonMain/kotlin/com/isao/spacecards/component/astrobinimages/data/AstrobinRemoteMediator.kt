package com.isao.spacecards.component.astrobinimages.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import arrow.core.raise.either
import com.isao.spacecards.astrobinimages.data.AstrobinImageEntityQueries
import com.isao.spacecards.component.astrobinimages.db.model.toEntityModel
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.component.astrobinimages.network.model.toDomainModel
import com.isao.spacecards.component.common.ApiFailureContainerException
import kotlinx.datetime.Instant

@OptIn(ExperimentalPagingApi::class)
internal class AstrobinRemoteMediator(
  private val queries: AstrobinImageEntityQueries,
  private val client: AstrobinImageApi,
  private val config: PagingConfig,
  private val startFromInstantExclusive: Instant?, //TODO do we need this is the mediator too?
) : RemoteMediator<Instant, AstrobinImage>() {
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Instant, AstrobinImage>,
  ): MediatorResult = either {
    val uploadedEarlierThan = when (loadType) {
      // Refresh resets paging and loads from the first page
      LoadType.REFRESH -> null
      // We can only move forward while paging
      LoadType.PREPEND -> return@either MediatorResult.Success(endOfPaginationReached = true)
      LoadType.APPEND -> {
        state.pages.lastOrNull { it.data.isNotEmpty() }?.nextKey
          ?: return@either MediatorResult.Success(endOfPaginationReached = true)
      }
    }

    // Treat uploaded time as an id for cursor-like paging.
    // This optimistically assumes there are no images uploaded at exactly the same time.
    // If it is not the case, some images might be skipped when loading new pages.
    // To fix it, we may query by less than or equals upload time rather than less than upload time.
    // This should load limit + 1 new images, 1 image being the same image as the last loaded image.
    // We'll then have to account for an edge case scenario when all loaded images
    // were uploaded at exactly the same time, causing the next page to load the same items.
    val images = client
      .getImages(
        uploadedEarlierThan = uploadedEarlierThan ?: startFromInstantExclusive,
        limit = config.pageSize,
        offset = 0, //TODO should we handle offset at all?
      ).bind()

    queries.transaction {
      if (loadType == LoadType.REFRESH) {
        queries.deleteAll(isBookmarked = false)
      }
      images.objects.forEach {
        queries.upsertEntity(it.toDomainModel().toEntityModel())
      }
    }
    return@either MediatorResult.Success(endOfPaginationReached = images.meta.next == null)
  }.fold({ MediatorResult.Error(ApiFailureContainerException(it)) }, { it })
}
