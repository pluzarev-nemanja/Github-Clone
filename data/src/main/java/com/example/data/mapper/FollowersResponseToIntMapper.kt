package com.example.data.mapper

import com.example.data.model.FollowerResponse
import com.example.domain.mapper.Mapper

class FollowersResponseToIntMapper : Mapper<List<FollowerResponse>, Int> {
    override suspend fun mappingObjects(input: List<FollowerResponse>): Int = input.size
}
