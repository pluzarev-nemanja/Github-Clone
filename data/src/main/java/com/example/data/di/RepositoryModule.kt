package com.example.data.di

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data.repository.AnalyticsRepositoryImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.MessagingRepositoryImpl
import com.example.data.repository.PullRequestRepositoryImpl
import com.example.data.repository.ReposRepositoryImpl
import com.example.data.repository.SearchRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AnalyticsRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.MessagingRepository
import com.example.domain.repository.PullRequestRepository
import com.example.domain.repository.ReposRepository
import com.example.domain.repository.SearchRepository
import com.example.domain.repository.UserRepository
import org.koin.dsl.module

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
val repositoryModule =
    module {

        factory<PullRequestRepository> {
            PullRequestRepositoryImpl(get(), get(), get(), get())
        }

        factory<ReposRepository> {
            ReposRepositoryImpl(get(), get(), get(), get())
        }

        factory<SearchRepository> {
            SearchRepositoryImpl(get(), get(), get(), get())
        }

        factory<UserRepository> {
            UserRepositoryImpl(get(), get(), get())
        }

        factory<AuthRepository> {
            AuthRepositoryImpl(get(), get())
        }
        factory<AnalyticsRepository> {
            AnalyticsRepositoryImpl(get())
        }

        factory<MessagingRepository> {
            MessagingRepositoryImpl(get())
        }
    }
