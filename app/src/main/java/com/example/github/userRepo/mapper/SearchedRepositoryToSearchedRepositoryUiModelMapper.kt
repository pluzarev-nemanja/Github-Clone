package com.example.github.userRepo.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.mapper.Mapper
import com.example.domain.model.SearchedRepository
import com.example.github.userRepo.model.UserRepoUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchedRepositoryToSearchedRepositoryUiModelMapper : Mapper<Flow<PagingData<SearchedRepository>>, Flow<PagingData<UserRepoUiModel>>> {
    override suspend fun mappingObjects(input: Flow<PagingData<SearchedRepository>>): Flow<PagingData<UserRepoUiModel>> =
        input.map { pagingRepository: PagingData<SearchedRepository> ->
            pagingRepository.map { searchedRepositories: SearchedRepository ->
                UserRepoUiModel(
                    id = 1,
                    repositoryName = searchedRepositories.name,
                    userName = searchedRepositories.authorName,
                    userIcon = searchedRepositories.authorIcon,
                    userFollowers = searchedRepositories.authorFollowers,
                    repositoryDescription = searchedRepositories.description ?: "No description found.",
                    issueCount = searchedRepositories.issueCount.toString(),
                    labelNames = searchedRepositories.labelNames,
                )
            }
        }
}
