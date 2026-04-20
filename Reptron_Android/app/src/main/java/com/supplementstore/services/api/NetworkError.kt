package com.supplementstore.services.api

sealed class NetworkError : Exception() {
    data object InvalidURL : NetworkError()
    data object InvalidResponse : NetworkError()
    data class HttpError(val statusCode: Int, override val message: String?) : NetworkError()
    data class DecodingError(val error: Throwable) : NetworkError()
    data class EncodingError(val error: Throwable) : NetworkError()
    data object NoData : NetworkError()
    data object Unauthorized : NetworkError()
    data class NetworkException(val error: Throwable) : NetworkError()

    override val message: String?
        get() = when (this) {
            is InvalidURL -> "Invalid URL"
            is InvalidResponse -> "Invalid response from server"
            is HttpError -> this.message ?: "HTTP Error: ${this.statusCode}"
            is DecodingError -> "Failed to decode response: ${error.message}"
            is EncodingError -> "Failed to encode request: ${error.message}"
            is NoData -> "No data received from server"
            is Unauthorized -> "Unauthorized access"
            is NetworkException -> "Network error: ${error.message}"
        }
}

data class APIErrorResponse(
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
) {
    val errorMessage: String
        get() = message ?: errors?.values?.flatten()?.joinToString(", ") ?: "An error occurred"
}
