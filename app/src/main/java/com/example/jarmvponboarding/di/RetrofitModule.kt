package com.example.jarmvponboarding.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitModule {

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://google.com/") // TODO:Replace later
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
