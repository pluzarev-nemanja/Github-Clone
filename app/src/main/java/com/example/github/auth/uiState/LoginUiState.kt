package com.example.github.auth.uiState

sealed class LoginUiState {
    data object Initial : LoginUiState()

    data object Loading : LoginUiState()

    data object Completed : LoginUiState()

    data object Canceled : LoginUiState()
}
