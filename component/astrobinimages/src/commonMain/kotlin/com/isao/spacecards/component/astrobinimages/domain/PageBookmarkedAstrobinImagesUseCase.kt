package com.isao.spacecards.component.astrobinimages.domain

import arrow.core.right
import isao.pager.Config
import isao.pager.Page
import isao.pager.Pager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class PageBookmarkedAstrobinImagesUseCase(
  private val astrobinImageRepository: AstrobinImageRepository,
) : KoinComponent {
  operator fun invoke(
    shouldSortAscending: Boolean,
    config: Config<Int>,
    currentPage: Flow<Int>,
  ): Flow<List<Page<Int, Nothing, AstrobinImage>>> {
    val nextKeyFlow = MutableSharedFlow<Int>()

    val pager = Pager(
      config = config,
      observeLocal = { page: Int, conf: Config<Int> ->
        astrobinImageRepository
          .observeBookmarkedPage(
            page,
            conf,
            shouldSortAscending,
          ).map { it.right() }
      },
      fetch = { _, _ ->
        // Violating ISP hard on this one. Will be fixed in one of Pager's TODOs.
        throw UnsupportedOperationException()
      },
      nextKey = nextKeyFlow,
      shouldRetry = emptyFlow(),
    )

    return flow {
      combine(
        currentPage,
        pager.flow,
      ) { currentPage, pages ->
        emit(pages)

        nextKeyFlow.emit(currentPage + 1)
      }.collect()
    }
  }
}
