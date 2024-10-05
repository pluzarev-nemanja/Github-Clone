package com.example.data.mapper

import com.example.data.model.FollowerResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FollowersResponseToIntMapperTest {
    private val followersResponseToIntMapper = FollowersResponseToIntMapper()

    @Test
    fun `given list of FollowerResponse When mappers is called Then actual is equal to expected`() =
        runTest {
            val response =
                listOf(
                    FollowerResponse(name = "asd"),
                    FollowerResponse(name = "asd"),
                    FollowerResponse(name = "asd"),
                )

            val actual = followersResponseToIntMapper.mappingObjects(response)
            assertEquals(3, actual)
        }

    @Test
    fun `given empty list When mapper is called Then actual is equal to expected`() =
        runTest {
            val emptyList = emptyList<FollowerResponse>()

            val actual = followersResponseToIntMapper.mappingObjects(emptyList)

            assertEquals(0, actual)
        }
}
