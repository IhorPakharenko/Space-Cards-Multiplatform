package com.isao.yfoo3.domain.repository

import com.isao.yfoo3.domain.model.FeedImage
import kotlinx.coroutines.flow.Flow

interface FeedImageRepository {
    fun getImages(): Flow<List<FeedImage>>

    fun getImage(id: String): Flow<FeedImage>

    suspend fun addRandomFeedImage()

    suspend fun deleteImage(id: String)
}