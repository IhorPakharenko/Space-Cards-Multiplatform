package com.isao.yfoo3.core.db.dao

import com.isao.yfoo3.core.db.model.FeedImageCached
import kotlinx.coroutines.flow.Flow

interface FeedImageDao {

    fun getFeedImages(): Flow<List<FeedImageCached>>

    fun getFeedImage(id: String): Flow<FeedImageCached>

    suspend fun saveFeedImage(item: FeedImageCached): Long

    suspend fun deleteFeedImage(id: String)
}