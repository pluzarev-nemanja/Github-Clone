package com.example.github.userRepo.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.mapper.Mapper
import com.example.domain.model.Repository
import com.example.github.userRepo.model.UserRepoUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryToUserRepoUiModelMapper :
    Mapper<Flow<PagingData<Repository>>, Flow<PagingData<UserRepoUiModel>>> {
    override suspend fun mappingObjects(input: Flow<PagingData<Repository>>): Flow<PagingData<UserRepoUiModel>> =
        input.map { pagingRepository: PagingData<Repository> ->
            pagingRepository.map { repository: Repository ->
                UserRepoUiModel(
                    id = repository.id,
                    repositoryName = repository.name,
                    userName = repository.authorName,
                    userIcon = repository.authorIcon,
                    userFollowers = repository.followers,
                    repositoryDescription = repository.description ?: "No description found.",
                    issueCount = repository.issuesCount.toString(),
                    labelNames = repository.labelNames,
                )
            }
        }
}
