package com.isao.spacecards.component.astrobinimages.network

import arrow.core.Either
import arrow.core.Either.Companion.catchOrThrow
import com.isao.spacecards.component.astrobinimages.data.AstrobinImageApi
import com.isao.spacecards.component.astrobinimages.network.model.ImagesResponse
import com.isao.spacecards.component.common.ApiFailure
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.datetime.Instant
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

internal class DefaultAstrobinImageApi(private val httpClient: HttpClient) : AstrobinImageApi {
  override suspend fun getImages(
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

  //TODO extract to a common datasource module
  inline fun <R> catchApiFailure(f: () -> R): Either<ApiFailure, R> = catchOrThrow<Exception, R>(f)
    .mapLeft {
      when (it) {
        is ResponseException -> ApiFailure.Server(it.response.status.value, it.message)
        is SerializationException -> ApiFailure.Serialization(it.message)
        is IOException -> ApiFailure.Connection
        else -> throw it
      }
    }
}
