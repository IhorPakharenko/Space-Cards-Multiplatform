package com.isao.spacecards.component.astrobinimages

import com.isao.spacecards.component.astrobinimages.data.DefaultAstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.PageAstrobinImagesUseCase
import com.isao.spacecards.component.astrobinimages.domain.PageBookmarkedAstrobinImagesUseCase
import com.isao.spacecards.component.astrobinimages.network.AstrobinImageApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.path
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val astrobinImagesComponentModule = module {
  factoryOf(::DefaultAstrobinImageRepository) bind AstrobinImageRepository::class

  factoryOf(::PageAstrobinImagesUseCase)

  factoryOf(::PageBookmarkedAstrobinImagesUseCase)

  factory { AstrobinImageApi(get(named(AstrobinImageApi.CLIENT))) }
  single(named(AstrobinImageApi.CLIENT)) {
    return@single get<HttpClient>().config {
      install(DefaultRequest) {
        url {
          host = AstrobinImageApi.HOST
          path(AstrobinImageApi.PATH)
          parameters.append(AstrobinImageApi.API_KEY, BuildConfig.ASTROBIN_API_KEY)
          parameters.append(AstrobinImageApi.API_SECRET, BuildConfig.ASTROBIN_API_SECRET)
        }
      }
    }
  }
}
