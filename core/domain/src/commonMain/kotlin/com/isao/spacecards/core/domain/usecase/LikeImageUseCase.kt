package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.common.extension.resultOf
import com.isao.spacecards.core.data.mapper.toLikedImage
import com.isao.spacecards.core.data.repository.FeedImageRepository
import com.isao.spacecards.core.data.repository.LikedImageRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class LikeImageUseCase(
  private val feedImageRepository: FeedImageRepository,
  private val likedImageRepository: LikedImageRepository,
) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    val feedImage = feedImageRepository.getImage(id).first()
    likedImageRepository.saveImage(feedImage.toLikedImage())
    feedImageRepository.deleteImage(feedImage.id)
  }
}
