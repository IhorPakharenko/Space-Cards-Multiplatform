package com.isao.spacecards.component.astrobinimages.domain

import arrow.core.right
import com.isao.spacecards.foundation.ApiFailure
import isao.pager.Config
import isao.pager.Page
import isao.pager.Pager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent

class PageAstrobinImagesUseCase(private val astrobinImageRepository: AstrobinImageRepository) :
  KoinComponent {
  operator fun invoke(
    config: Config<Instant?>,
    shouldRetry: Flow<Instant?>,
    currentPage: Flow<Instant?>,
  ): Flow<List<Page<Instant?, ApiFailure, AstrobinImage>>> {
    val nextKeyFlow = MutableSharedFlow<Instant?>()

    val pager = Pager(
      config = config,
      observeLocal = { instant: Instant?, conf: Config<Instant?> ->
        astrobinImageRepository.observePage(instant, conf).map { it.right() }
      },
      fetch = { key, conf ->
        astrobinImageRepository.fetchPage(key, conf)
      },
      nextKey = nextKeyFlow,
      shouldRetry = shouldRetry,
    )

    return flow {
      combine(
        currentPage,
        pager.flow,
      ) { currentPage, pages ->
        emit(pages)

        val nextKey = pages
          .firstOrNull { it.key == currentPage }
          ?.lastValidItems
          ?.lastOrNull()
          ?.uploadedAt
        if (nextKey != null) {
          nextKeyFlow.emit(nextKey)
        }
      }.collect()
    }
  }
}
