package com.example.github.userRepo.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.RepositoryDetails
import com.example.github.userRepo.model.RepositoryDetailsUiModel

class RepositoryDetailsToRepositoryDetailsUiModelMapper : Mapper<RepositoryDetails, RepositoryDetailsUiModel> {
    override suspend fun mappingObjects(input: RepositoryDetails): RepositoryDetailsUiModel =
        RepositoryDetailsUiModel(
            repositoryName = input.name,
            userName = input.authorName,
            userIcon = input.authorIcon,
            userFollowers = input.followers.plus(" Followers"),
            repositoryDescription = input.description ?: "No description found.",
            repositoryForks = input.forks.toString().plus(" Forks"),
            watchers = input.watchers.toString().plus(" Watchers"),
            topics = input.topics,
        )
}
