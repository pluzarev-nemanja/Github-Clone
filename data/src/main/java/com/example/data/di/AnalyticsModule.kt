package com.example.data.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.koin.dsl.module

val analyticsModule =
    module {

        single { Firebase.analytics }
    }
