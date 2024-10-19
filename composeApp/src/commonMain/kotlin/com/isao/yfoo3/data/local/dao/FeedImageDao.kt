package com.isao.yfoo3.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isao.yfoo3.data.local.model.FeedImageCached
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedImageDao {

    @Query("SELECT * FROM FeedImageCached")
    fun getFeedImages(): Flow<List<FeedImageCached>>

    @Query("SELECT * FROM FeedImageCached WHERE id = :id")
    fun getFeedImage(id: String): Flow<FeedImageCached>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFeedImage(item: FeedImageCached): Long

    @Query("DELETE FROM FeedImageCached WHERE id = :id")
    suspend fun deleteFeedImage(id: String)
}