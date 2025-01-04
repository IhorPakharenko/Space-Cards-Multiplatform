package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.common.extension.resultOf
import com.isao.spacecards.component.images.domain.repository.FeedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    feedImageRepository.deleteImage(id)
  }
}
