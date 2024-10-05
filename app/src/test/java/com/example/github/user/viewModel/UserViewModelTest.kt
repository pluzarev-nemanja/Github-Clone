package com.example.github.user.viewModel

import com.example.domain.model.User
import com.example.domain.useCases.GetAuthenticatedUserUseCase
import com.example.domain.useCases.GetUserUseCase
import com.example.github.user.mapper.ThrowableToUserUiStateErrorMapper
import com.example.github.user.mapper.UserToUserUiModelMapper
import com.example.github.user.model.UserUiModel
import com.example.github.user.uiState.UserUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserViewModelTest {
    private val getUserUseCase = mockk<GetUserUseCase>()
    private val getAuthenticatedUserUseCase = mockk<GetAuthenticatedUserUseCase>()
    private val userToUserUiModelMapper = mockk<UserToUserUiModelMapper>()
    private val throwableToUserUiStateErrorMapper = mockk<ThrowableToUserUiStateErrorMapper>(relaxed = true)

    private val userViewModel =
        UserViewModel(
            getUserUseCase = getUserUseCase,
            getAuthenticatedUserUseCase = getAuthenticatedUserUseCase,
            userToUserUiModelMapper = userToUserUiModelMapper,
            throwableToUserUiStateErrorMapper = throwableToUserUiStateErrorMapper,
        )

    @Test
    fun `given userName When getUser is called Then actual is equal to expected`() =
        runTest {
            val userName = "user"

            val user =
                User(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    following = 1,
                    followers = 1,
                    location = "",
                )
            val userUiModel =
                UserUiModel(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    following = "1 Following",
                    followers = "1 Followers",
                    location = "",
                )

            val expected = UserUiState.Success(data = userUiModel)

            coEvery { getUserUseCase(any()) } returns user
            coEvery { userToUserUiModelMapper.mappingObjects(any()) } returns userUiModel

            userViewModel.getUser(userName)

            val actual = userViewModel.userUiState.value
            assertEquals(expected, actual)

            coVerify { getUserUseCase(any()) }
            coVerify { userToUserUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given userName with exception When getUser is called Then actual is equal to expected`() =
        runTest {
            val spyViewModel = spyk(userViewModel)
            val exception = IllegalStateException()
            val userName = "name"

            val expected = UserUiState.Error.Unknown("")

            coEvery { getUserUseCase(any()) } throws exception
            coEvery { spyViewModel["convertError"](exception) } returns expected

            spyViewModel.getUser(userName)

            val actual = spyViewModel.userUiState.value

            assertEquals(expected, actual)

            coVerify { getUserUseCase(any()) }
            coVerify { spyViewModel["convertError"](exception) }
        }

    @Test
    fun `given token When getAuthenticatedUser is called Then actual is equal to expected`() =
        runTest {
            val token = "token"

            val user =
                User(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    following = 1,
                    followers = 1,
                    location = "",
                )
            val userUiModel =
                UserUiModel(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    following = "1 Following",
                    followers = "1 Followers",
                    location = "",
                )
            val expected = UserUiState.Success(data = userUiModel)

            coEvery { getAuthenticatedUserUseCase(any()) } returns user
            coEvery { userToUserUiModelMapper.mappingObjects(any()) } returns userUiModel

            userViewModel.getAuthenticatedUser(token)

            val actual = userViewModel.userUiState.value

            assertEquals(expected, actual)

            coVerify { getAuthenticatedUserUseCase(any()) }
            coVerify { userToUserUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given token with exception When getAuthenticatedUser is called Then actual is equal to expected`() =
        runTest {
            val spyViewModel = spyk(userViewModel)
            val exception = IllegalStateException()
            val token = "token"

            val expected = UserUiState.Error.Unknown("")

            coEvery { getAuthenticatedUserUseCase(any()) } throws exception
            coEvery { spyViewModel["convertError"](exception) } returns expected

            spyViewModel.getAuthenticatedUser(token)

            val actual = spyViewModel.userUiState.value

            assertEquals(expected, actual)

            coVerify { getAuthenticatedUserUseCase(any()) }
            coVerify { spyViewModel["convertError"](exception) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
