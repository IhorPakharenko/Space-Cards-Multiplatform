package com.isao.yfoo3.core.domain.usecase

import com.isao.yfoo3.core.common.extension.resultOf
import com.isao.yfoo3.core.data.repository.LikedImageRepository
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