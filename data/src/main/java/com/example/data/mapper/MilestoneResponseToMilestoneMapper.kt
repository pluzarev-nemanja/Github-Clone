package com.example.data.mapper

import com.example.data.model.MilestoneResponse
import com.example.domain.mapper.Mapper

class MilestoneResponseToMilestoneMapper : Mapper<MilestoneResponse?, String?> {
    override suspend fun mappingObjects(input: MilestoneResponse?): String? = input?.title
}
