package com.example.jarmvponboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jarmvponboarding.data.remote.OnboardingApiService
import com.example.jarmvponboarding.domain.repository.OnboardingRepositoryImpl
import com.example.jarmvponboarding.domain.usecase.GetOnboardingDataUseCase
import com.example.jarmvponboarding.presentation.OnboardingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val apiService = OnboardingApiService.create()
                val repository = OnboardingRepositoryImpl(apiService)
                val useCase = GetOnboardingDataUseCase(repository)
                val viewModel: OnboardingViewModel = viewModel(factory = OnboardingViewModel.Factory(useCase))
                OnboardingNavHost(viewModel)
            }
        }
    }
}
