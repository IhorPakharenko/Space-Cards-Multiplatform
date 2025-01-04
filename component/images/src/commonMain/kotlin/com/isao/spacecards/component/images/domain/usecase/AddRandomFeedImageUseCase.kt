package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.common.extension.resultOf
import com.isao.spacecards.component.images.domain.repository.FeedImageRepository
import org.koin.core.annotation.Factory

@Factory
class AddRandomFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(): Result<Unit> = resultOf {
    feedImageRepository.addRandomFeedImage()
  }
}
