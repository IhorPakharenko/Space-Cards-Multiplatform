package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.common.extension.resultOf
import com.isao.spacecards.core.data.repository.FeedImageRepository
import org.koin.core.annotation.Factory

@Factory
class AddRandomFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(): Result<Unit> = resultOf {
    feedImageRepository.addRandomFeedImage()
  }
}
