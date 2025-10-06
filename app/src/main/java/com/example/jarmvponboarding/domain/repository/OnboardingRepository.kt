package com.example.jarmvponboarding.domain.repository

import com.example.jarmvponboarding.domain.model.OnboardingData
import com.example.jarmvponboarding.util.Resource


// Interface to support mocking
interface OnboardingRepository {
    suspend fun getOnboardingData(): Resource<OnboardingData>
}
