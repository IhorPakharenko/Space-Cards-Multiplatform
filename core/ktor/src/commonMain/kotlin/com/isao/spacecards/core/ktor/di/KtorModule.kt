package com.isao.spacecards.core.ktor.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val ktorModule = module {
  single {
    HttpClient {
      expectSuccess = true

      install(ContentNegotiation) {
        json(
          Json {
            isLenient = true
            ignoreUnknownKeys = true
          },
        )
      }
      install(HttpCache)
      install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
      }
      install(HttpTimeout) {
        requestTimeoutMillis = 10_000
      }
    }
  }
}
