package com.example.data.mapper

import com.example.data.model.LabelResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.Label

class LabelResponseToLabelMapper : Mapper<List<LabelResponse>, List<Label>> {
    override suspend fun mappingObjects(input: List<LabelResponse>): List<Label> =
        input.map { label: LabelResponse ->
            Label(
                color = label.color,
                default = label.default,
                description = label.description,
                id = label.id,
                name = label.name,
                nodeId = label.nodeId,
                url = label.url,
            )
        }
}
