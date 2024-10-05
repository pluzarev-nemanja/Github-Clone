package com.example.data.mapper

import com.example.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class FireBaseUserToAuthUserMapperTest {
    private val firebaseAuthMapper = FireBaseUserToAuthUserMapper()
    private val mockFirebaseUser = mockk<FirebaseUser>()

    @Test
    fun `given FirebaseUser When mapper is invoked Then actual is equal to expected`() =
        runTest {
            val expected = AuthUser(uid = "123", displayName = "John Doe", email = "john@gmail.com")

            every { mockFirebaseUser.uid } returns "123"
            every { mockFirebaseUser.displayName } returns "John Doe"
            every { mockFirebaseUser.email } returns "john@gmail.com"

            val actual = firebaseAuthMapper.mappingObjects(mockFirebaseUser)

            assertEquals(expected, actual)

            verify { mockFirebaseUser.uid }
            verify { mockFirebaseUser.displayName }
            verify { mockFirebaseUser.email }
        }

    @Test
    fun `given null When mapper is invoked Then actual is equal to expected`() =
        runTest {
            val actual = firebaseAuthMapper.mappingObjects(null)
            assertEquals(null, actual)
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
