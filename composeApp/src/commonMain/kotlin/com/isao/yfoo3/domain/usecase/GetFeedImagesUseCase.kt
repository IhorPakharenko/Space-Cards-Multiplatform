package com.isao.yfoo3.domain.usecase

import com.isao.yfoo3.domain.model.FeedImage
import com.isao.yfoo3.domain.repository.FeedImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetFeedImagesUseCase(
    private val feedImageRepository: FeedImageRepository
) {
    operator fun invoke(): Flow<Result<List<FeedImage>>> {
        return feedImageRepository
            .getImages()
            .map { Result.success(it) }
            .catch {
                emit(Result.failure(it))
            }
    }
}