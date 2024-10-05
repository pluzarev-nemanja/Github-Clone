package com.example.data.di

import com.example.data.mapper.FireBaseUserToAuthUserMapper
import com.example.data.mapper.FollowersResponseToIntMapper
import com.example.data.mapper.LabelResponseToLabelMapper
import com.example.data.mapper.LabelResponseToStringMapper
import com.example.data.mapper.MilestoneResponseToMilestoneMapper
import com.example.data.mapper.PullRequestDetailsResponseToPullRequestDetailsMapper
import com.example.data.mapper.PullRequestResponseToPullRequestMapper
import com.example.data.mapper.RepositoryDetailsResponseToRepositoryDetailsMapper
import com.example.data.mapper.RepositoryResponseToRepositoryMapper
import com.example.data.mapper.RequestedReviewerResponseToRequestedReviewerMapper
import com.example.data.mapper.SearchedRepositoryResponseToSearchedRepositoryMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.mapper.TopicResponseToTopicMapper
import com.example.data.mapper.UserResponseToUserMapper
import org.koin.dsl.module

val mapperModule =
    module {

        factory { UserResponseToUserMapper() }
        factory { TopicResponseToTopicMapper() }
        factory { SearchedRepositoryResponseToSearchedRepositoryMapper(get()) }
        factory { RepositoryDetailsResponseToRepositoryDetailsMapper(get(), get()) }
        factory { RepositoryResponseToRepositoryMapper(get()) }
        factory { LabelResponseToLabelMapper() }
        factory { RequestedReviewerResponseToRequestedReviewerMapper() }
        factory { MilestoneResponseToMilestoneMapper() }
        factory { PullRequestResponseToPullRequestMapper(get(), get()) }
        factory { PullRequestDetailsResponseToPullRequestDetailsMapper(get(), get(), get()) }
        factory { FollowersResponseToIntMapper() }
        factory { LabelResponseToStringMapper() }
        factory { ThrowableToErrorModelMapper() }
        factory { FireBaseUserToAuthUserMapper() }
    }
