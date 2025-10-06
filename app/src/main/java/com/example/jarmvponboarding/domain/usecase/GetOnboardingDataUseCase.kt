package com.example.jarmvponboarding.domain.usecase

import com.example.jarmvponboarding.domain.model.OnboardingData
import com.example.jarmvponboarding.domain.repository.OnboardingRepository
import com.example.jarmvponboarding.util.Resource


// Use case to encapsulate the business logic of getting data from api.
class GetOnboardingDataUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Resource<OnboardingData> {
        return repository.getOnboardingData()
    }
}