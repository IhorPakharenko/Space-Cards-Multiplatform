package com.isao.spacecards.component.astrobinimages

import com.isao.spacecards.component.astrobinimages.data.AstrobinImageApi
import com.isao.spacecards.component.astrobinimages.data.AstrobinRemoteMediator
import com.isao.spacecards.component.astrobinimages.data.DefaultAstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImageRepository
import com.isao.spacecards.component.astrobinimages.domain.ObservePagedAstrobinImagesUseCase
import com.isao.spacecards.component.astrobinimages.network.DefaultAstrobinImageApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.parameters
import io.ktor.http.path
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

//TODO add component to the name
val astrobinImagesModule = module {
  factoryOf(::DefaultAstrobinImageRepository) bind AstrobinImageRepository::class

  factoryOf(::DefaultAstrobinImageApi) bind AstrobinImageApi::class

  factoryOf(::ObservePagedAstrobinImagesUseCase)

  factory { params -> AstrobinRemoteMediator(get(), get(), params.get(), params.getOrNull()) }

  single(named(AstrobinImageApi.CLIENT)) {
    val client: HttpClient = get()
    return@single client.config {
      install(DefaultRequest) {
        url {
          host = AstrobinImageApi.HOST
          path(AstrobinImageApi.PATH)
          parameters {
            append(AstrobinImageApi.API_KEY, BuildConfig.ASTROBIN_API_KEY)
            append(AstrobinImageApi.API_SECRET, BuildConfig.ASTROBIN_API_SECRET)
          }
        }
      }
    }
  }
}
