package com.example.jarmvponboarding.data.mapper

import com.example.jarmvponboarding.data.remote.OnboardingApiService
import com.example.jarmvponboarding.domain.model.OnboardingData
import com.example.jarmvponboarding.domain.repository.OnboardingRepository


import com.example.jarmvponboarding.data.remote.dto.toOnboardingData
import com.example.jarmvponboarding.util.Resource

class OnboardingRepositoryImpl(
    private val apiService: OnboardingApiService
) : OnboardingRepository {
    override suspend fun getOnboardingData(): Resource<OnboardingData> {
        return try {
            val response = apiService.getOnboardingData()
            Resource.Success(data = response.toOnboardingData())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = "Failed to load data: ${e.message}")
        }
    }
}
