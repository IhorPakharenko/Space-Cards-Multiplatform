package com.isao.spacecards.component.astrobinimages.network

import arrow.core.Either
import com.isao.spacecards.component.common.catchApiFailure
import com.isao.spacecards.foundation.ApiFailure
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.datetime.Instant

internal class AstrobinImageApi(private val httpClient: HttpClient) {
  companion object {
    const val CLIENT = "astrobin"
    const val HOST = "www.astrobin.com"
    const val PATH = "api/v1/"
    const val API_KEY = "api_key"
    const val API_SECRET = "api_secret"
  }

  suspend fun getImages(
    uploadedEarlierThan: Instant?,
    limit: Int,
    offset: Int,
  ): Either<ApiFailure, ImagesResponse> = catchApiFailure {
    httpClient
      .get("image") {
        parameter("uploaded__lt", uploadedEarlierThan)
        parameter("limit", limit)
        parameter("offset", offset)
      }.body()
  }
}
