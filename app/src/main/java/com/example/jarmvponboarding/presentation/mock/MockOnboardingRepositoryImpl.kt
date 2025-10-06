package com.example.jarmvponboarding.presentation.mock


import android.content.Context
import com.example.jarmvponboarding.data.remote.dto.OnboardingResponseDto
import com.example.jarmvponboarding.data.remote.dto.toOnboardingData
import com.example.jarmvponboarding.domain.model.OnboardingData
import com.example.jarmvponboarding.domain.repository.OnboardingRepository
import com.example.jarmvponboarding.util.Resource
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

// A mock repository for local development and previews.
// It reads the JSON from the local assets folder.
class MockOnboardingRepositoryImpl(
    private val context: Context
) : OnboardingRepository {
    override suspend fun getOnboardingData(): Resource<OnboardingData> {
        // Simulate network delay
        delay(1500)

        return try {
            val jsonString = context.assets.open("education-metadata.json").bufferedReader().use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            val responseDto = json.decodeFromString<OnboardingResponseDto>(jsonString)

            Resource.Success(data = responseDto.toOnboardingData())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = "Error loading mock data: ${e.message}")
        }
    }
}
