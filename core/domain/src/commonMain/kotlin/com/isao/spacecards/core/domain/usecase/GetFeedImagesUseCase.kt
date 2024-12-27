package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.data.repository.FeedImageRepository
import com.isao.spacecards.core.domain.util.asResult
import com.isao.spacecards.core.model.FeedImage
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetFeedImagesUseCase(private val feedImageRepository: FeedImageRepository) {
  operator fun invoke(): Flow<Result<List<FeedImage>>> = feedImageRepository
    .getImages()
    .asResult()
}
