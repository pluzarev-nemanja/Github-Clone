package com.example.data.mapper

import com.example.data.model.MilestoneResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MilestoneResponseToMilestoneMapperTest {
    private val milestoneResponseToMilestoneMapper = MilestoneResponseToMilestoneMapper()

    @Test
    fun `given milestoneResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val milestone = MilestoneResponse(title = "milestone")
            val actual = milestoneResponseToMilestoneMapper.mappingObjects(milestone)
            val expected = "milestone"

            assertEquals(expected, actual)
        }

    @Test
    fun `given null When mapper is called Then actual is equal to expected`() =
        runTest {
            val actual = milestoneResponseToMilestoneMapper.mappingObjects(null)
            assertEquals(null, actual)
        }
}
