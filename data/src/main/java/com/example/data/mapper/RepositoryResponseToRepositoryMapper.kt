package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.flatMap
import com.example.data.model.LabelResponse
import com.example.data.model.RepositoryWithIssue
import com.example.domain.mapper.Mapper
import com.example.domain.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryResponseToRepositoryMapper(
    private val labelResponseToStringMapper: LabelResponseToStringMapper,
) : Mapper<Response, Model> {
    override suspend fun mappingObjects(input: Response): Model =
        input.map { pagingData: PagingData<RepositoryWithIssue> ->
            pagingData.flatMap { pagingRepository ->
                pagingRepository.repositories.flatMap { repository ->
                    pagingRepository.openIssuesTotalCount.map { issueCount ->
                        Repository(
                            id = repository.id,
                            name = repository.name,
                            authorIcon = repository.owner.authorIcon,
                            authorName = repository.owner.authorName,
                            followers = repository.owner.followers,
                            description = repository.description,
                            issuesCount = issueCount,
                            labelNames = pagingRepository.labels.mapLabelResponse(),
                        )
                    }
                }
            }
        }

    private suspend fun List<LabelResponse>.mapLabelResponse() = labelResponseToStringMapper.mappingObjects(this)
}

private typealias Response = Flow<PagingData<RepositoryWithIssue>>
private typealias Model = Flow<PagingData<Repository>>
