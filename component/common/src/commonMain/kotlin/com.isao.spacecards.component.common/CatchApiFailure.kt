package com.isao.spacecards.component.common

import arrow.core.Either
import arrow.core.Either.Companion.catchOrThrow
import com.isao.spacecards.foundation.ApiFailure
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

inline fun <R> catchApiFailure(f: () -> R): Either<ApiFailure, R> = catchOrThrow<Exception, R>(f)
  .mapLeft {
    when (it) {
      is ResponseException -> ApiFailure.Server(it.response.status.value, it.message)
      is SerializationException -> ApiFailure.Serialization(it.message)
      is IOException -> ApiFailure.Connection
      else -> throw it
    }
  }
