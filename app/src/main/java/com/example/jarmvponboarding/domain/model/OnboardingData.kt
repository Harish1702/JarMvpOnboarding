package com.example.jarmvponboarding.domain.model

import androidx.compose.ui.graphics.Color


data class OnboardingData(
    val toolbarTitle: String,
    val cards: List<OnboardingCard>,
    val expandCardStayInterval: Long,
    val bottomToCenterTranslationInterval: Long,
    val collapseCardTiltInterval: Long,
    val collapseExpandIntroInterval: Long,
    val introTitle: String,
    val introSubtitle: String
)

data class OnboardingCard(
    val collapsedText: String,
    val expandedText: String,
    val imageUrl: String,
    val backgroundColor: Color,
    val cardColor: Color,
    val strokeStartColor: Color,
    val strokeEndColor: Color
)