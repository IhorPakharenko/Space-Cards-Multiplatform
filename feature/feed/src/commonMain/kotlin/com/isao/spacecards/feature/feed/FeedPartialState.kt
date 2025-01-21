package com.isao.spacecards.feature.feed

import androidx.paging.PagingData
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

//TODO consider renaming to reducer. Also, consider ditching the whole concept
sealed class FeedPartialState {
  data class StartFromInstantSet(val instant: Instant?) : FeedPartialState()

  data class ItemsFetched(val items: Flow<PagingData<AstrobinImage>>) : FeedPartialState()
}
