package com.example.data.di

import com.example.data.dataSource.AuthDataSourceImpl
import com.example.domain.dataSource.AuthDataSource
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val authModule =
    module {

        single { FirebaseAuth.getInstance() }
        single<AuthDataSource> { AuthDataSourceImpl(get()) }
    }
