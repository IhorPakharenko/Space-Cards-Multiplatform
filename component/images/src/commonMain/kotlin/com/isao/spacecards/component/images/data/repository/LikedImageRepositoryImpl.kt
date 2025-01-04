package com.isao.spacecards.component.images.data.repository

import com.isao.spacecards.component.images.data.mapper.toDomainModel
import com.isao.spacecards.component.images.data.mapper.toEntityModel
import com.isao.spacecards.component.images.domain.model.LikedImage
import com.isao.spacecards.component.images.domain.repository.LikedImageRepository
import com.isao.spacecards.core.db.dao.LikedImageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [LikedImageRepository::class])
internal class LikedImageRepositoryImpl(private val likedImageDao: LikedImageDao) :
  LikedImageRepository {
  override fun getImages(): Flow<List<LikedImage>> =
    likedImageDao.getLikedImages().map { imagesCached ->
      imagesCached.map { it.toDomainModel() }
    }

  override fun getImages(
    shouldSortAscending: Boolean,
    limit: Int,
    offset: Int,
  ): Flow<List<LikedImage>> = likedImageDao
    .getLikedImages(shouldSortAscending, limit, offset)
    .map { imagesCached ->
      imagesCached.map { it.toDomainModel() }
    }

  override suspend fun saveImage(item: LikedImage) {
    likedImageDao.saveLikedImage(item.toEntityModel())
  }

  override suspend fun deleteImage(id: String) {
    likedImageDao.deleteLikedImage(id)
  }
}
