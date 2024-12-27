package com.isao.spacecards.core.data.repository

import com.isao.spacecards.core.model.LikedImage
import kotlinx.coroutines.flow.Flow

interface LikedImageRepository {
  fun getImages(): Flow<List<LikedImage>>

  fun getImages(
    shouldSortAscending: Boolean,
    limit: Int,
    offset: Int,
  ): Flow<List<LikedImage>>

  suspend fun saveImage(item: LikedImage)

  suspend fun deleteImage(id: String)
}
