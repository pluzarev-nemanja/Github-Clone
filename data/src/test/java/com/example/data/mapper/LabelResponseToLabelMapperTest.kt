package com.example.data.mapper

import com.example.data.model.LabelResponse
import com.example.domain.model.Label
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LabelResponseToLabelMapperTest {
    private val labelResponseToLabelMapper = LabelResponseToLabelMapper()

    @Test
    fun `given list of label Responses When mapper is called Then actual is equal to expected`() =
        runTest {
            val labelList =
                listOf(
                    LabelResponse(
                        color = "",
                        default = true,
                        description = "",
                        id = 0L,
                        name = "asd",
                        nodeId = "asd",
                        url = "asd",
                    ),
                )

            val expected =
                listOf(
                    Label(
                        color = "",
                        default = true,
                        description = "",
                        id = 0L,
                        name = "asd",
                        nodeId = "asd",
                        url = "asd",
                    ),
                )

            val actual = labelResponseToLabelMapper.mappingObjects(labelList)

            assertEquals(expected, actual)
        }

    @Test
    fun `given empty list When mapper is called Then actual is equal to expected`() =
        runTest {
            val list = emptyList<LabelResponse>()
            val actual = labelResponseToLabelMapper.mappingObjects(list)

            assertEquals(emptyList<Label>(), actual)
        }
}
