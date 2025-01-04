package com.isao.spacecards.component.images.domain.usecase

import com.isao.spacecards.component.common.extension.resultOf
import com.isao.spacecards.component.images.domain.repository.LikedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteLikedImageUseCase(private val likedImageRepository: LikedImageRepository) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    likedImageRepository.deleteImage(id)
  }
}
