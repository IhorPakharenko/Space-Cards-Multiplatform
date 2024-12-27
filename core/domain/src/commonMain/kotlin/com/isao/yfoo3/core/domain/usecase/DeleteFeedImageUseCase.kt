package com.isao.yfoo3.core.domain.usecase

import com.isao.yfoo3.core.common.extension.resultOf
import com.isao.yfoo3.core.data.repository.FeedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteFeedImageUseCase(private val feedImageRepository: FeedImageRepository) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    feedImageRepository.deleteImage(id)
  }
}
