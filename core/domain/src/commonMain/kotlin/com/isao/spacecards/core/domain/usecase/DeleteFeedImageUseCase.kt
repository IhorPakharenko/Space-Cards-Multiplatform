package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.common.extension.resultOf
import com.isao.spacecards.core.data.repository.FeedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    feedImageRepository.deleteImage(id)
  }
}
