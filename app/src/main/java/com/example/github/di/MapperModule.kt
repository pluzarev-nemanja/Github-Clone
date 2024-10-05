package com.example.github.di

import com.example.github.auth.mapper.AuthUserToAuthUserUiModelMapper
import com.example.github.pullRequest.mapper.LabelToLabelUiModelMapper
import com.example.github.pullRequest.mapper.PullRequestDetailsToPullRequestDetailsUiModelMapper
import com.example.github.pullRequest.mapper.PullRequestToPullRequestUiModelMapper
import com.example.github.pullRequest.mapper.ReviewerToReviewerUiModelMapper
import com.example.github.pullRequest.mapper.ThrowableToPullRequestDetailsUiStateMapper
import com.example.github.topic.mapper.TopicToTopicUiModelMapper
import com.example.github.user.mapper.ThrowableToUserUiStateErrorMapper
import com.example.github.user.mapper.UserToUserUiModelMapper
import com.example.github.userRepo.mapper.RepositoryDetailsToRepositoryDetailsUiModelMapper
import com.example.github.userRepo.mapper.RepositoryToUserRepoUiModelMapper
import com.example.github.userRepo.mapper.SearchedRepositoryToSearchedRepositoryUiModelMapper
import com.example.github.userRepo.mapper.ThrowableToCommitUiStateErrorMapper
import com.example.github.userRepo.mapper.ThrowableToRepositoryDetailsUiStateErrorMapper
import org.koin.dsl.module

val mapperModule =
    module {

        factory { RepositoryDetailsToRepositoryDetailsUiModelMapper() }
        factory { RepositoryToUserRepoUiModelMapper() }
        factory { SearchedRepositoryToSearchedRepositoryUiModelMapper() }
        factory { PullRequestToPullRequestUiModelMapper(get(), get()) }
        factory { ReviewerToReviewerUiModelMapper() }
        factory { LabelToLabelUiModelMapper() }
        factory { PullRequestDetailsToPullRequestDetailsUiModelMapper(get(), get()) }
        factory { TopicToTopicUiModelMapper() }
        factory { UserToUserUiModelMapper() }
        factory { ThrowableToRepositoryDetailsUiStateErrorMapper() }
        factory { ThrowableToUserUiStateErrorMapper() }
        factory { ThrowableToPullRequestDetailsUiStateMapper() }
        factory { AuthUserToAuthUserUiModelMapper() }
        factory { ThrowableToCommitUiStateErrorMapper() }
    }
