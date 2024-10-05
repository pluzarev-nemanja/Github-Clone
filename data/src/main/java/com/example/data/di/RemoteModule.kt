package com.example.data.di

import com.example.data.remote.PullRequestApi
import com.example.data.remote.RepositoryApi
import com.example.data.remote.SearchApi
import com.example.data.remote.UserApi
import com.example.data.util.Constants.BASE_URL
import com.example.data.util.Constants.TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteModule =
    module {

        single {
            createRetrofit(BASE_URL)
                .create(RepositoryApi::class.java)
        }

        single {
            createRetrofit(BASE_URL)
                .create(PullRequestApi::class.java)
        }

        single {
            createRetrofit(BASE_URL)
                .create(SearchApi::class.java)
        }

        single {
            createRetrofit(BASE_URL)
                .create(UserApi::class.java)
        }
    }

private fun createRetrofit(baseUrl: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createInterceptor())
        .build()

private fun createInterceptor() =
    OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .addInterceptor(
            Interceptor { chain ->
                val request: Request =
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "token $TOKEN")
                        .build()
                chain.proceed(request)
            },
        )
        .build()
