package com.example.jarmvponboarding.data.remote

import com.example.jarmvponboarding.data.remote.dto.OnboardingResponseDto
import retrofit2.http.GET

interface OnboardingApiService {
    // TODO: add endpoint
    @GET("education-metadata")
    suspend fun getOnboardingData(): OnboardingResponseDto
}
