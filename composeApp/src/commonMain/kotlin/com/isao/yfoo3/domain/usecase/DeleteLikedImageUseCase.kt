package com.isao.yfoo3.domain.usecase

import com.isao.yfoo3.core.extensions.resultOf
import com.isao.yfoo3.domain.repository.LikedImageRepository
import org.koin.core.annotation.Single

@Single
class DeleteLikedImageUseCase(
    private val likedImageRepository: LikedImageRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return resultOf {
            likedImageRepository.deleteImage(id)
        }
    }
}