package com.isao.yfoo3.data.dao

import com.isao.yfoo3.data.model.LikedImageCached
import kotlinx.coroutines.flow.Flow

interface LikedImageDao {

    fun getLikedImages(): Flow<List<LikedImageCached>>

    fun getLikedImages(
        shouldSortAscending: Boolean,
        limit: Int,
        offset: Int
    ): Flow<List<LikedImageCached>>

    suspend fun saveLikedImage(item: LikedImageCached)

    suspend fun deleteLikedImage(id: String)
}