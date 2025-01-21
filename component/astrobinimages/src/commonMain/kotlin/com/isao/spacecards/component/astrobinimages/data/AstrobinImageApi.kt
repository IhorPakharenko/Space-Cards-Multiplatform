package com.isao.spacecards.component.astrobinimages.data

import arrow.core.Either
import com.isao.spacecards.component.astrobinimages.network.model.ImagesResponse
import com.isao.spacecards.component.common.ApiFailure
import kotlinx.datetime.Instant

interface AstrobinImageApi {
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
  ): Either<ApiFailure, ImagesResponse>
}
