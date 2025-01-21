package com.isao.spacecards.component.astrobinimages.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.isao.spacecards.component.astrobinimages.data.AstrobinRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class ObservePagedAstrobinImagesUseCase(
  private val astrobinImageRepository: AstrobinImageRepository,
) : KoinComponent {
  @OptIn(ExperimentalPagingApi::class)
  operator fun invoke(
    config: PagingConfig,
    startFromInstantExclusive: Instant?,
  ): Flow<PagingData<AstrobinImage>> {
    val remoteMediator by inject<AstrobinRemoteMediator> {
      parametersOf(
        config,
        startFromInstantExclusive,
      )
    }
    return Pager(
      config = config,
      remoteMediator = remoteMediator,
      pagingSourceFactory = { astrobinImageRepository.getPagingSource(startFromInstantExclusive) },
    ).flow
  }
}
