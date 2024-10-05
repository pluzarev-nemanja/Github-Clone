package com.example.di

import com.example.data.di.analyticsModule
import com.example.data.di.authModule
import com.example.data.di.mapperModule
import com.example.data.di.messagingModule
import com.example.data.di.remoteModule
import com.example.data.di.repositoryModule
import com.example.domain.di.useCaseModule

val modules = useCaseModule.plus(mapperModule).plus(remoteModule).plus(repositoryModule).plus(
    authModule
).plus(analyticsModule).plus(messagingModule)