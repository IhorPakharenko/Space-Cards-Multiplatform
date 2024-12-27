package com.isao.yfoo3.core.domain.usecase

import com.isao.yfoo3.core.data.repository.LikedImageRepository
import com.isao.yfoo3.core.domain.util.asResult
import com.isao.yfoo3.core.model.LikedImage
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetLikedImagesUseCase(private val likedImageRepository: LikedImageRepository) {
  operator fun invoke(
    shouldSortAscending: Boolean,
    limit: Int,
    offset: Int,
  ): Flow<Result<List<LikedImage>>> = likedImageRepository
    .getImages(
      shouldSortAscending = shouldSortAscending,
      limit = limit,
      offset = offset,
    ).asResult()
}
