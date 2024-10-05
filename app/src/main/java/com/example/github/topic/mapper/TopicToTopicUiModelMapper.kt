package com.example.github.topic.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.mapper.Mapper
import com.example.domain.model.Topic
import com.example.github.topic.model.TopicUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicToTopicUiModelMapper : Mapper<Flow<PagingData<Topic>>, Flow<PagingData<TopicUiModel>>> {
    override suspend fun mappingObjects(input: Flow<PagingData<Topic>>): Flow<PagingData<TopicUiModel>> =
        input.map { pagingTopic: PagingData<Topic> ->
            pagingTopic.map { topic: Topic ->
                TopicUiModel(
                    name = topic.name ?: "Unknown",
                    shortDescription = topic.shortDescription ?: "No description found.",
                    createdAt = topic.createdAt ?: "No date found.",
                    createdBy = topic.createdBy ?: "Unknown",
                    totalCont = topic.totalCount,
                )
            }
        }
}
