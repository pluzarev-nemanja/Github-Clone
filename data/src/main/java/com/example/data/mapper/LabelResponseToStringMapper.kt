package com.example.data.mapper

import com.example.data.model.LabelResponse
import com.example.domain.mapper.Mapper

class LabelResponseToStringMapper : Mapper<List<LabelResponse>, List<String>> {
    override suspend fun mappingObjects(input: List<LabelResponse>): List<String> =
        input.map { label ->
            label.name
        }
}
