package com.isao.yfoo3.domain.usecase

import com.isao.yfoo3.core.extensions.resultOf
import com.isao.yfoo3.domain.repository.FeedImageRepository
import org.koin.core.annotation.Factory

@Factory
class AddRandomFeedImageUseCase(
    private val feedImageRepository: FeedImageRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return resultOf {
            feedImageRepository.addRandomFeedImage()
        }
    }
}
