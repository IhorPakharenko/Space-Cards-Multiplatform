package com.isao.yfoo3.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.isao.yfoo3.data.local.model.LikedImageCached
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedImageDao {

    @Query("SELECT * FROM LikedImageCached")
    fun getLikedImages(): Flow<List<LikedImageCached>>

    @Query(
        """
        SELECT * FROM LikedImageCached ORDER BY
        CASE WHEN :shouldSortAscending = 1 THEN dateAdded END ASC,
        CASE WHEN :shouldSortAscending = 0 THEN dateAdded END DESC
        LIMIT :limit OFFSET :offset
        """
    )
    fun getLikedImages(
        shouldSortAscending: Boolean,
        limit: Int,
        offset: Int
    ): Flow<List<LikedImageCached>>

    @Upsert
    suspend fun saveLikedImage(item: LikedImageCached)

    @Query("DELETE FROM LikedImageCached WHERE id = :id")
    suspend fun deleteLikedImage(id: String)
}