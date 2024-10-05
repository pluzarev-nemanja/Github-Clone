package com.example.data.di

import com.example.data.dataSource.MessageTokenDataSourceImpl
import com.example.domain.dataSource.MessageTokenDataSource
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val messagingModule =
    module {

        single { FirebaseMessaging.getInstance() }
        single<MessageTokenDataSource> { MessageTokenDataSourceImpl(androidApplication(), get()) }
    }
