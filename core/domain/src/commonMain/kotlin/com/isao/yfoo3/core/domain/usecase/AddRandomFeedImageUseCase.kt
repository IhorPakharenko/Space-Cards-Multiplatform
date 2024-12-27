package com.isao.yfoo3.core.domain.usecase

import com.isao.yfoo3.core.common.extension.resultOf
import com.isao.yfoo3.core.data.repository.FeedImageRepository
import org.koin.core.annotation.Factory

@Factory
class AddRandomFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(): Result<Unit> = resultOf {
    feedImageRepository.addRandomFeedImage()
  }
}
