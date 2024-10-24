package com.isao.yfoo3.data.testdoubles

import com.isao.yfoo3.data.dao.LikedImageDao
import com.isao.yfoo3.data.model.LikedImageCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single(binds = [LikedImageDao::class])
open class FakeLikedImageDao : LikedImageDao {
    private val likedImagesFlow = MutableStateFlow<List<LikedImageCached>>(emptyList())

    override fun getLikedImages(): Flow<List<LikedImageCached>> = likedImagesFlow

    override fun getLikedImages(
        shouldSortAscending: Boolean,
        limit: Int,
        offset: Int
    ): Flow<List<LikedImageCached>> {
        return likedImagesFlow.map { images ->
            val sortedImages = if (shouldSortAscending) {
                images.sortedBy { it.dateAdded }
            } else {
                images.sortedByDescending { it.dateAdded }
            }
            // Room treats -1 as no limit
            sortedImages.drop(offset).take(limit.takeIf { it >= 0 } ?: Int.MAX_VALUE)
        }
    }

    override suspend fun saveLikedImage(item: LikedImageCached) {
        likedImagesFlow.update { currentImages ->
            currentImages.toMutableList().apply {
                removeIf { it.id == item.id } // Remove if already exists
                add(item)
            }
        }
    }

    override suspend fun deleteLikedImage(id: String) {
        likedImagesFlow.update { currentImages ->
            currentImages.filterNot { it.id == id }
        }
    }
}