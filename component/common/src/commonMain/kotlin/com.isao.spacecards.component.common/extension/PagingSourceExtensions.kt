package com.isao.spacecards.component.common.extension

import androidx.paging.PagingSource
import androidx.paging.PagingState

fun <Key : Any, OldValue : Any, NewValue : Any> PagingSource<Key, OldValue>.mapValue(
  toNew: (OldValue) -> NewValue,
  toOld: (NewValue) -> OldValue,
): PagingSource<Key, NewValue> {
  val decorated = this
  return object : PagingSource<Key, NewValue>() {
    override fun getRefreshKey(state: PagingState<Key, NewValue>): Key? = decorated.getRefreshKey(
      PagingState(
        pages = state.pages.map { page ->
          LoadResult.Page(
            data = page.data.map(toOld),
            prevKey = page.prevKey,
            nextKey = page.nextKey,
          )
        },
        anchorPosition = state.anchorPosition,
        config = state.config,
        leadingPlaceholderCount = extractLeadingPlaceholderCount(state)!!,
      ),
    )

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, NewValue> =
      when (val result = decorated.load(params)) {
        is LoadResult.Error -> LoadResult.Error(result.throwable)
        is LoadResult.Invalid -> LoadResult.Invalid()
        is LoadResult.Page -> LoadResult.Page(
          data = result.data.map(toNew),
          prevKey = result.prevKey,
          nextKey = result.nextKey,
        )
      }

    override val jumpingSupported: Boolean
      get() = decorated.jumpingSupported

    override val keyReuseSupported: Boolean
      get() = decorated.keyReuseSupported

    // Thanks for making this private, Google!
    private fun extractLeadingPlaceholderCount(state: PagingState<*, *>): Int? {
      val regex = Regex("""leadingPlaceholderCount=(\d+)""")
      val matchResult = regex.find(state.toString())
      return matchResult
        ?.groups
        ?.get(1)
        ?.value
        ?.toIntOrNull()
    }
  }
}
