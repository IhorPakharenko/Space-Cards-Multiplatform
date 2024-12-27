package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.data.repository.LikedImageRepository
import com.isao.spacecards.core.domain.util.asResult
import com.isao.spacecards.core.model.LikedImage
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
