package com.example.github.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AuthUser
import com.example.domain.useCases.GetCurrentUserUseCase
import com.example.domain.useCases.SignInWithGithubUseCase
import com.example.domain.useCases.SignOutUseCase
import com.example.github.auth.mapper.AuthUserToAuthUserUiModelMapper
import com.example.github.auth.model.AuthUserUiModel
import com.example.github.auth.uiState.AuthUiState
import com.example.github.auth.uiState.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val signInWithGithubUseCase: SignInWithGithubUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authUserToAuthUserUiModelMapper: AuthUserToAuthUserUiModelMapper,
) : ViewModel() {
    private val mutableAuthUiState: MutableStateFlow<AuthUiState> =
        MutableStateFlow(AuthUiState.Empty)
    val authUiState: StateFlow<AuthUiState> = mutableAuthUiState.asStateFlow()

    private val mutableLoginUiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> = mutableLoginUiState.asStateFlow()

    private val mutableIsUserLoggedIn: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = mutableIsUserLoggedIn.asStateFlow()

    init {
        getCurrentUser()
    }

    fun updateIsUserLoggedIn(isLoggedIn: Boolean) {
        mutableIsUserLoggedIn.value = isLoggedIn
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            mutableAuthUiState.value = AuthUiState.Loading
            runCatching {
                getCurrentUserUseCase()
            }.mapCatching { authUser: AuthUser? ->
                authUserToAuthUserUiModelMapper.mappingObjects(authUser)
            }.mapCatching { authUserUiModel: AuthUserUiModel? ->
                mutableAuthUiState.value = AuthUiState.Success(data = authUserUiModel)
            }.onFailure { exception: Throwable ->
                mutableAuthUiState.value = AuthUiState.Error.Unknown(message = exception.message)
                Timber.e("Error occurred : ${exception.message}")
            }
        }
    }

    fun updateLoginState(loginUiState: LoginUiState) {
        mutableLoginUiState.value = loginUiState
    }

    fun updateAuthState(authUiState: AuthUiState) {
        mutableAuthUiState.value = authUiState
    }

    fun signInWithGithub(token: String) {
        viewModelScope.launch {
            signInWithGithubUseCase(token = token)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            mutableAuthUiState.value = AuthUiState.Empty
            mutableLoginUiState.value = LoginUiState.Initial
            signOutUseCase()
        }
    }
}
