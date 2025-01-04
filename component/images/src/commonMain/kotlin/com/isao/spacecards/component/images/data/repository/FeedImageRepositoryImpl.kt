package com.isao.spacecards.component.images.data.repository

import com.isao.spacecards.component.images.data.mapper.toDomainModel
import com.isao.spacecards.component.images.data.mapper.toEntityModel
import com.isao.spacecards.component.images.domain.model.FeedImage
import com.isao.spacecards.component.images.domain.model.ImageSource
import com.isao.spacecards.component.images.domain.repository.FeedImageRepository
import com.isao.spacecards.core.db.dao.FeedImageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single
import kotlin.random.Random

@Single(binds = [FeedImageRepository::class])
internal class FeedImageRepositoryImpl(private val feedImageDao: FeedImageDao) :
  FeedImageRepository {
  companion object {
    const val MIN_ITEM_COUNT = 20
  }

  override fun getImages(): Flow<List<FeedImage>> = feedImageDao
    .getFeedImages()
    .map { imagesCached ->
      imagesCached.map { it.toDomainModel() }
    }.onEach { items ->
      val itemsToFetchCount = MIN_ITEM_COUNT - items.size
      repeat(itemsToFetchCount) {
        addRandomFeedImage()
      }
    }

  override fun getImage(id: String): Flow<FeedImage> = feedImageDao.getFeedImage(id).map {
    it.toDomainModel()
  }

  /**
   * Generates items and tries to insert them until an item with a non-repeated url is generated
   */
  override suspend fun addRandomFeedImage() {
    var insertedRowId: Long
    do {
      // Disable adding new images by This Waifu Does Not Exist for now.
      // Right now, their quality contrasts too much with other sources and the app itself.
      val source = (ImageSource.entries - ImageSource.THIS_WAIFU_DOES_NOT_EXIST).random()
      val imageId = source.getRandomImageId(Random)
      insertedRowId = feedImageDao.saveFeedImage(
        FeedImage(
          id = "${source}_$imageId",
          imageId = imageId,
          source = source,
        ).toEntityModel(),
      )
    } while (insertedRowId == -1L)
  }

  override suspend fun deleteImage(id: String) {
    feedImageDao.deleteFeedImage(id)
  }
}
