package com.example.jarmvponboarding

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jarmvponboarding.presentation.OnboardingViewModel

@Composable
fun OnboardingNavHost(viewModel: OnboardingViewModel) {
    val navController = rememberNavController()

    // TODO: Move it routes class to separate file instead of hardcoding later
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(viewModel = viewModel, onNavigateToLanding = { navController.navigate("landing")})
        }
        composable("landing") {
            LandingScreen(onBackButtonClick = { navController.navigate("onboarding")})
        }
    }
}
