
package com.example.jarmvponboarding.data.remote

import com.example.jarmvponboarding.data.remote.dto.OnboardingResponseDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

interface OnboardingApiService {

    @GET("_assets/shared/education-metadata.json")
    suspend fun getOnboardingData(): OnboardingResponseDto

    companion object {
        private const val BASE_URL = "https://myjar.app/"

        fun create(): OnboardingApiService {
            val contentType = "application/json".toMediaType()
            val json = Json { ignoreUnknownKeys = true }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
                .create(OnboardingApiService::class.java)
        }
    }
}
