package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.common.extension.resultOf
import com.isao.spacecards.component.images.domain.model.FeedImage
import com.isao.spacecards.component.images.domain.model.LikedImage
import com.isao.spacecards.component.images.domain.repository.FeedImageRepository
import com.isao.spacecards.component.images.domain.repository.LikedImageRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
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

private fun FeedImage.toLikedImage() = LikedImage(
  id = id,
  imageId = imageId,
  source = source,
  dateAdded = Clock.System.now(),
)
