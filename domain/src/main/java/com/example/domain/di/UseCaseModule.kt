package com.example.domain.di

import com.example.domain.useCases.GetAllRepositoriesUseCase
import com.example.domain.useCases.GetAuthRepositoriesUseCase
import com.example.domain.useCases.GetAuthenticatedUserUseCase
import com.example.domain.useCases.GetCurrentUserUseCase
import com.example.domain.useCases.GetPullRequestDetailsUseCase
import com.example.domain.useCases.GetPullRequestsUseCase
import com.example.domain.useCases.GetRepositoryCommitsUseCase
import com.example.domain.useCases.GetRepositoryDetailsUseCase
import com.example.domain.useCases.GetUserUseCase
import com.example.domain.useCases.LogEventUseCase
import com.example.domain.useCases.ReadMessageTokenUseCase
import com.example.domain.useCases.SaveMessageTokenUseCase
import com.example.domain.useCases.SearchRepositoriesUseCase
import com.example.domain.useCases.SearchTopicsUseCase
import com.example.domain.useCases.SignInWithGithubUseCase
import com.example.domain.useCases.SignOutUseCase
import org.koin.dsl.module

val useCaseModule =
    module {

        factory { GetUserUseCase(get()) }
        factory { SearchRepositoriesUseCase(get()) }
        factory { GetAllRepositoriesUseCase(get()) }
        factory { GetPullRequestsUseCase(get()) }
        factory { GetPullRequestDetailsUseCase(get()) }
        factory { GetRepositoryDetailsUseCase(get()) }
        factory { SearchTopicsUseCase(get()) }
        factory { SignInWithGithubUseCase(get()) }
        factory { GetCurrentUserUseCase(get()) }
        factory { SignOutUseCase(get()) }
        factory { GetAuthenticatedUserUseCase(get()) }
        factory { GetAuthRepositoriesUseCase(get()) }
        factory { LogEventUseCase(get()) }
        factory { SaveMessageTokenUseCase(get()) }
        factory { ReadMessageTokenUseCase(get()) }
        factory { GetRepositoryCommitsUseCase(get()) }
    }
