package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.images.domain.model.LikedImage
import com.isao.spacecards.component.images.domain.repository.LikedImageRepository
import com.isao.spacecards.component.images.domain.util.asResult
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
