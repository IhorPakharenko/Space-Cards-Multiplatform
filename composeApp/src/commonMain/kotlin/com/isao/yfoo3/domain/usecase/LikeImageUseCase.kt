package com.isao.yfoo3.domain.usecase

import com.isao.yfoo3.core.extensions.resultOf
import com.isao.yfoo3.data.local.mapper.toLikedImage
import com.isao.yfoo3.domain.repository.FeedImageRepository
import com.isao.yfoo3.domain.repository.LikedImageRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class LikeImageUseCase(
    private val feedImageRepository: FeedImageRepository,
    private val likedImageRepository: LikedImageRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return resultOf {
            val feedImage = feedImageRepository.getImage(id).first()
            likedImageRepository.saveImage(feedImage.toLikedImage())
            feedImageRepository.deleteImage(feedImage.id)
        }
    }
}