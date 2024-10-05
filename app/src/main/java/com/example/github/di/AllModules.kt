package com.example.github.di

import com.example.di.modules

val allModules = modules.plus(viewModelModule).plus(mapperModule).plus(notificationManagerModule)
