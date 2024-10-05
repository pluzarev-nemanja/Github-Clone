package com.example.github.pullRequest.mapper

import com.example.domain.model.Label
import com.example.github.pullRequest.model.LabelUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LabelToLabelUiModelMapperTest {
    private val labelToLabelUiModelMapper = LabelToLabelUiModelMapper()

    @Test
    fun `given label list When mapper is called Then actual is equal to expected`() =
        runTest {
            val labels =
                listOf(
                    Label(
                        color = "",
                        description = "",
                        default = false,
                        id = 1L,
                        name = "",
                        nodeId = "",
                        url = "",
                    ),
                )
            val expected =
                listOf(
                    LabelUiModel(
                        labelColor = "",
                        labelName = "",
                    ),
                )
            val actual = labelToLabelUiModelMapper.mappingObjects(labels)

            assertEquals(expected, actual)
        }

    @Test
    fun `given empty label list When mapper is called Then actual is equal to expected`() =
        runTest {
            val labels = emptyList<Label>()
            val expected = emptyList<LabelUiModel>()
            val actual = labelToLabelUiModelMapper.mappingObjects(labels)

            assertEquals(expected, actual)
        }
}
