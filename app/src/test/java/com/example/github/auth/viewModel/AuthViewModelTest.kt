package com.example.github.auth.viewModel

import com.example.domain.model.AuthUser
import com.example.domain.useCases.GetCurrentUserUseCase
import com.example.domain.useCases.SignInWithGithubUseCase
import com.example.domain.useCases.SignOutUseCase
import com.example.github.auth.mapper.AuthUserToAuthUserUiModelMapper
import com.example.github.auth.model.AuthUserUiModel
import com.example.github.auth.uiState.AuthUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthViewModelTest {
    private val signInWithGithubUseCase = mockk<SignInWithGithubUseCase>()
    private val getCurrentUserUseCase = mockk<GetCurrentUserUseCase>()
    private val signOutUseCase = mockk<SignOutUseCase>()
    private val authUserToAuthUserUiModelMapper = mockk<AuthUserToAuthUserUiModelMapper>()

    private val authViewModel =
        AuthViewModel(
            signInWithGithubUseCase = signInWithGithubUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            signOutUseCase = signOutUseCase,
            authUserToAuthUserUiModelMapper = authUserToAuthUserUiModelMapper,
        )

    @Test
    fun `when getCurrentUser is called Then actual is equal to expected`() =
        runTest {
            val authUser =
                AuthUser(
                    uid = "",
                    displayName = "",
                    email = "",
                )

            val authUserUiModel =
                AuthUserUiModel(
                    uid = "",
                    displayName = "",
                    email = "",
                )

            coEvery { getCurrentUserUseCase() } returns authUser
            coEvery { authUserToAuthUserUiModelMapper.mappingObjects(any()) } returns authUserUiModel

            authViewModel.getCurrentUser()

            val expected = AuthUiState.Success(data = authUserUiModel)
            val actual = authViewModel.authUiState.value

            assertEquals(expected, actual)

            coVerify { getCurrentUserUseCase() }
            coVerify { authUserToAuthUserUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `when getCurrentUse is called with exception Then AuthUiState is set to Error`() =
        runTest {
            val exception = IllegalStateException("")

            val expected = AuthUiState.Error.Unknown(message = "")

            coEvery { getCurrentUserUseCase() } throws exception

            authViewModel.getCurrentUser()

            val actual = authViewModel.authUiState.value

            assertEquals(expected, actual)

            coVerify { getCurrentUserUseCase() }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
