package com.isao.yfoo3.data.repository

import com.isao.yfoo3.data.dao.LikedImageDao
import com.isao.yfoo3.data.local.mapper.toDomainModel
import com.isao.yfoo3.data.local.mapper.toEntityModel
import com.isao.yfoo3.domain.model.LikedImage
import com.isao.yfoo3.domain.repository.LikedImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [LikedImageRepository::class])
class LikedImageRepositoryImpl(
    private val likedImageDao: LikedImageDao,
) : LikedImageRepository {

    override fun getImages(): Flow<List<LikedImage>> {
        return likedImageDao.getLikedImages().map { imagesCached ->
            imagesCached.map { it.toDomainModel() }
        }
    }

    override fun getImages(
        shouldSortAscending: Boolean,
        limit: Int,
        offset: Int
    ): Flow<List<LikedImage>> {
        return likedImageDao.getLikedImages(shouldSortAscending, limit, offset)
            .map { imagesCached ->
                imagesCached.map { it.toDomainModel() }
            }
    }

    override suspend fun saveImage(item: LikedImage) {
        likedImageDao.saveLikedImage(item.toEntityModel())
    }

    override suspend fun deleteImage(id: String) {
        likedImageDao.deleteLikedImage(id)
    }
}