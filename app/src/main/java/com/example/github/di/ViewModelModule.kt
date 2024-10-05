package com.example.github.di

import com.example.github.MainViewModel
import com.example.github.auth.viewModel.AuthViewModel
import com.example.github.pullRequest.viewModel.PullRequestViewModel
import com.example.github.topic.viewModel.TopicsViewModel
import com.example.github.user.viewModel.UserViewModel
import com.example.github.userRepo.viewModel.UserRepoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {

        viewModel { UserRepoViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        viewModel { PullRequestViewModel(get(), get(), get(), get(), get()) }
        viewModel { TopicsViewModel(get(), get()) }
        viewModel { UserViewModel(get(), get(), get(), get()) }
        viewModel { AuthViewModel(get(), get(), get(), get()) }
        viewModel { MainViewModel(get(), get()) }
    }
