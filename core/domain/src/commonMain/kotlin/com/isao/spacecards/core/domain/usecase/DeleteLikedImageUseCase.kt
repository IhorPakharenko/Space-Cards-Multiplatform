package com.isao.spacecards.core.domain.usecase

import com.isao.spacecards.core.common.extension.resultOf
import com.isao.spacecards.core.data.repository.LikedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteLikedImageUseCase(private val likedImageRepository: LikedImageRepository) {
  suspend operator fun invoke(id: String): Result<Unit> = resultOf {
    likedImageRepository.deleteImage(id)
  }
}
