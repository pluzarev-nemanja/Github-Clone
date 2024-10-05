package com.example.github.di

import com.example.domain.notification.NotificationSenderManager
import com.example.github.core.notification.NotificationSenderManagerImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val notificationManagerModule =
    module {

        factory<NotificationSenderManager> { NotificationSenderManagerImpl(androidApplication()) }
    }
