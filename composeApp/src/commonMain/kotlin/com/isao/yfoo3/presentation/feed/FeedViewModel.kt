package com.isao.yfoo3.presentation.feed


import com.isao.yfoo3.core.MviViewModel
import com.isao.yfoo3.core.extensions.resultOf
import com.isao.yfoo3.domain.model.FeedImage
import com.isao.yfoo3.domain.model.ImageSource
import com.isao.yfoo3.presentation.feed.mapper.toPresentationModel
import com.isao.yfoo3.presentation.feed.model.FeedItemDisplayable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Single


@KoinViewModel
class FeedViewModel(
    private val likeImageUseCase: LikeImageUseCase,
    private val deleteFeedImageUseCase: DeleteFeedImageUseCase,
    private val getFeedImagesUseCase: GetFeedImagesUseCase,
) : MviViewModel<FeedUiState, FeedPartialState, Nothing, FeedIntent>(
    FeedUiState()
) {
    init {
        observeContinuousChanges(getItems())
    }

    override fun mapIntents(intent: FeedIntent): Flow<FeedPartialState> = when (intent) {
        is FeedIntent.Like -> likeItem(intent.item)
        is FeedIntent.Dislike -> dislikeItem(intent.item)
    }

    override fun reduceUiState(
        previousState: FeedUiState,
        partialState: FeedPartialState
    ): FeedUiState = when (partialState) {
        is FeedPartialState.ItemsFetched -> previousState.copy(
            items = partialState.items,
            isLoading = false,
            isError = false
        )

        FeedPartialState.ItemsLoading -> previousState.copy(
            isLoading = true
        )

        is FeedPartialState.ItemDismissed -> previousState.copy(
            items = previousState.items.filterNot { item ->
                item.id == partialState.item.id
            },
            isLoading = false,
            isError = false
        )

        is FeedPartialState.Error -> previousState.copy(
            isError = true,
        )
    }

    private fun getItems(): Flow<FeedPartialState> =
        getFeedImagesUseCase()
            .map { result ->
                result.fold(
                    onSuccess = { items ->
                        FeedPartialState.ItemsFetched(
                            items.map { it.toPresentationModel() }
                        )
                    },
                    onFailure = {
                        FeedPartialState.Error(it)
                    }
                )
            }
            .onStart {
                emit(FeedPartialState.ItemsLoading)
            }

    private fun likeItem(item: FeedItemDisplayable): Flow<FeedPartialState> = flow {
        emit(FeedPartialState.ItemDismissed(item))
        likeImageUseCase(item.id)
            .onFailure {
                emit(FeedPartialState.Error(it))
            }
    }

    private fun dislikeItem(item: FeedItemDisplayable): Flow<FeedPartialState> = flow {
        emit(FeedPartialState.ItemDismissed(item))
        deleteFeedImageUseCase(item.id)
            .onFailure {
                emit(FeedPartialState.Error(it))
            }
    }
}
@Single
class LikeImageUseCase {
    operator fun invoke(any: Any): Result<Unit> {
        return resultOf {
            Unit
        }
    }
}
@Single
class DeleteFeedImageUseCase {
    operator fun invoke(any: Any): Result<Unit> {
        return resultOf {
            Unit
        }
    }
}
@Single
class GetFeedImagesUseCase {
    operator fun invoke(): Flow<Result<List<FeedImage>>> {
        return flowOf(
            resultOf {
                List(20) {
                    FeedImage(
                        id = (1001 + it).toString(),
                        imageId = (1001 + it).toString(),
                        source = ImageSource.THIS_NIGHT_SKY_DOES_NOT_EXIST
                    )
                }
            }
        )
    }
}
