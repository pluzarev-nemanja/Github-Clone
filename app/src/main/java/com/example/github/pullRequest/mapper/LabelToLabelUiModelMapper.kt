package com.example.github.pullRequest.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.Label
import com.example.github.pullRequest.model.LabelUiModel

class LabelToLabelUiModelMapper : Mapper<List<Label>, List<LabelUiModel>> {
    override suspend fun mappingObjects(input: List<Label>): List<LabelUiModel> =
        input.map { label: Label ->
            LabelUiModel(
                labelColor = label.color,
                labelName = label.name,
            )
        }
}
