package com.example.data.mapper

import com.example.data.model.RepositoryDetailsResponse
import com.example.data.remote.RepositoryApi
import com.example.domain.mapper.Mapper
import com.example.domain.model.RepositoryDetails

class RepositoryDetailsResponseToRepositoryDetailsMapper(
    private val api: RepositoryApi,
    private val followersResponseToIntMapper: FollowersResponseToIntMapper,
) :
    Mapper<RepositoryDetailsResponse, RepositoryDetails> {
    override suspend fun mappingObjects(input: RepositoryDetailsResponse): RepositoryDetails =
        RepositoryDetails(
            name = input.name,
            authorName = input.owner.authorName,
            authorIcon = input.owner.authorIcon,
            followers = followersResponseToIntMapper.mappingObjects(api.getFollowers(input.owner.followers)).toString(),
            description = input.description,
            forks = input.forks,
            watchers = input.watchers,
            topics = input.topics,
        )
}
