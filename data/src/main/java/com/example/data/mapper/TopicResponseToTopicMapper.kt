package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.flatMap
import androidx.paging.map
import com.example.data.model.TopicResponse
import com.example.data.model.TopicsResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicResponseToTopicMapper :
    Mapper<PagingTopicResponse, PagingTopicModel> {
    override suspend fun mappingObjects(input: PagingTopicResponse): PagingTopicModel =
        input.map { topicResponse: PagingData<TopicsResponse> ->

            topicResponse.flatMap {
                val count = it.totalCount

                it.topics.map { topic: TopicResponse ->

                    Topic(
                        name = topic.name,
                        shortDescription = topic.shortDescription,
                        createdAt = topic.createdAt,
                        createdBy = topic.createdBy,
                        totalCount = count,
                    )
                }
            }
        }
}

private typealias PagingTopicResponse = Flow<PagingData<TopicsResponse>>
private typealias PagingTopicModel = Flow<PagingData<Topic>>
