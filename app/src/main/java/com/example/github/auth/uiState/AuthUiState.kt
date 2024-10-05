package com.example.github.auth.uiState

import com.example.github.auth.model.AuthUserUiModel

sealed class AuthUiState {
    data object Loading : AuthUiState()

    data object Empty : AuthUiState()

    data class Success(val data: AuthUserUiModel? = null, val token: String = "") : AuthUiState()

    sealed class Error : AuthUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
