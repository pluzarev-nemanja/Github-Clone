package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.flatMap
import com.example.data.model.LabelResponse
import com.example.data.model.SearchedRepositoryWithIssue
import com.example.domain.mapper.Mapper
import com.example.domain.model.SearchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchedRepositoryResponseToSearchedRepositoryMapper(
    private val labelResponseToStringMapper: LabelResponseToStringMapper,
) :
    Mapper<PagingResponse, PagingModel> {
    override suspend fun mappingObjects(input: PagingResponse): PagingModel =
        input.map { pagingData: PagingData<SearchedRepositoryWithIssue> ->

            pagingData.flatMap { repositoryResponse: SearchedRepositoryWithIssue ->
                repositoryResponse.repositories.flatMap { searchedRepository ->
                    repositoryResponse.openIssuesTotalCount.map { issueCount ->
                        SearchedRepository(
                            name = searchedRepository.name,
                            authorIcon = searchedRepository.owner.authorIcon,
                            authorName = searchedRepository.owner.authorName,
                            authorFollowers = searchedRepository.owner.followers,
                            description = searchedRepository.description,
                            issueCount = issueCount,
                            labelNames = repositoryResponse.labels.mapLabelResponse(),
                        )
                    }
                }
            }
        }

    private suspend fun List<LabelResponse>.mapLabelResponse() = labelResponseToStringMapper.mappingObjects(this)
}

private typealias PagingResponse = Flow<PagingData<SearchedRepositoryWithIssue>>
private typealias PagingModel = Flow<PagingData<SearchedRepository>>
