package com.isao.spacecards.component.images.domain.repository

import com.isao.spacecards.component.images.domain.model.FeedImage
import kotlinx.coroutines.flow.Flow

interface FeedImageRepository {
  fun getImages(): Flow<List<FeedImage>>

  fun getImage(id: String): Flow<FeedImage>

  suspend fun addRandomFeedImage()

  suspend fun deleteImage(id: String)
}
