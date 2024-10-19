package com.isao.yfoo3.domain.usecase

import com.isao.yfoo3.core.extensions.resultOf
import com.isao.yfoo3.domain.repository.FeedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteFeedImageUseCase(
    private val feedImageRepository: FeedImageRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return resultOf {
            feedImageRepository.deleteImage(id)
        }
    }
}