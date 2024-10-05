package com.example.domain.model

sealed class ErrorResponse : Throwable() {
    data class Host(override val message: String = "Something went wrong on server.") : ErrorResponse()

    data class Network(override val message: String = "Check your internet connection!") : ErrorResponse()

    data class Unknown(override val message: String = "Unknown error occurred.") : ErrorResponse()
}
