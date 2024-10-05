package com.example.github.user.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.useCases.GetAuthenticatedUserUseCase
import com.example.domain.useCases.GetUserUseCase
import com.example.github.user.mapper.ThrowableToUserUiStateErrorMapper
import com.example.github.user.mapper.UserToUserUiModelMapper
import com.example.github.user.model.UserUiModel
import com.example.github.user.uiState.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class UserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getAuthenticatedUserUseCase: GetAuthenticatedUserUseCase,
    private val userToUserUiModelMapper: UserToUserUiModelMapper,
    private val throwableToUserUiStateErrorMapper: ThrowableToUserUiStateErrorMapper,
) : ViewModel() {
    private val mutableUserUiState: MutableStateFlow<UserUiState> =
        MutableStateFlow(UserUiState.Loading)

    val userUiState: StateFlow<UserUiState> = mutableUserUiState.asStateFlow()

    fun getUser(userName: String) {
        viewModelScope.launch {
            mutableUserUiState.value = UserUiState.Loading
            runCatching {
                getUserUseCase(userName = userName)
            }.mapCatching { user: User ->
                userToUserUiModelMapper.mappingObjects(user)
            }.mapCatching { userUiModel: UserUiModel ->
                mutableUserUiState.value =
                    UserUiState.Success(
                        data = userUiModel,
                    )
            }.onFailure { exception ->
                mutableUserUiState.value = exception.convertError()
                Timber.e("Unknown error occurred! ${mutableUserUiState.value}")
            }
        }
    }

    fun getAuthenticatedUser(token: String) {
        viewModelScope.launch {
            mutableUserUiState.value = UserUiState.Loading

            runCatching {
                getAuthenticatedUserUseCase(token = token)
            }.mapCatching { user: User ->
                userToUserUiModelMapper.mappingObjects(user)
            }.mapCatching { userUiModel: UserUiModel ->
                mutableUserUiState.value =
                    UserUiState.Success(
                        data = userUiModel,
                    )
            }.onFailure { exception ->
                mutableUserUiState.value = exception.convertError()
                Timber.e("Unknown error occurred! ${mutableUserUiState.value}")
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun Throwable.convertError(): UserUiState.Error =
        throwableToUserUiStateErrorMapper.runCatching {
            throwableToUserUiStateErrorMapper.mappingObjects(this@convertError)
        }.onFailure { throwable ->
            Timber.e("Error : $throwable")
        }.getOrThrow()
}
