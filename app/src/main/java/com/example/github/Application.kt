package com.example.github

import android.app.Application
import com.example.github.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(allModules)
        }
        Timber.plant(Timber.DebugTree())
    }
}
