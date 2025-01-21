package com.isao.spacecards.component.common

sealed interface ApiFailure {
  // Host not resolved, timeout, no Internet
  data object Connection : ApiFailure

  // Invalid JSON
  data class Serialization(val message: String?) : ApiFailure

  // 300..599 response codes
  data class Server(val code: Int, val message: String?) : ApiFailure
}

/**
 * Must only be used as a way to pass Failures (that do not extend Throwable)
 * into Paging or other incompatible libraries
 */
data class ApiFailureContainerException(val failure: ApiFailure) : Exception()
