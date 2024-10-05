package com.example.data.mapper

import com.example.data.model.LabelResponse
import com.example.domain.model.Label
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LabelResponseToStringMapperTest {
    private val labelResponseToStringMapper = LabelResponseToStringMapper()

    @Test
    fun `given list of LabelResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val labelList =
                listOf(
                    LabelResponse(
                        color = "",
                        default = true,
                        description = "",
                        id = 0L,
                        name = "name",
                        nodeId = "asd",
                        url = "asd",
                    ),
                    LabelResponse(
                        color = "",
                        default = true,
                        description = "",
                        id = 0L,
                        name = "name2",
                        nodeId = "asd",
                        url = "asd",
                    ),
                )

            val expected = listOf("name", "name2")

            val actual = labelResponseToStringMapper.mappingObjects(labelList)

            assertEquals(expected, actual)
        }

    @Test
    fun `given empty list When mapper is called Then actual is equal to expected`() =
        runTest {
            val list = emptyList<LabelResponse>()

            val actual = labelResponseToStringMapper.mappingObjects(list)
            assertEquals(emptyList<Label>(), actual)
        }
}
