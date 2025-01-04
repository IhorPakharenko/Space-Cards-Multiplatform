package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.images.domain.model.FeedImage
import com.isao.spacecards.component.images.domain.repository.FeedImageRepository
import com.isao.spacecards.component.images.domain.util.asResult
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetFeedImagesUseCase(private val feedImageRepository: FeedImageRepository) {
  operator fun invoke(): Flow<Result<List<FeedImage>>> = feedImageRepository
    .getImages()
    .asResult()
}
