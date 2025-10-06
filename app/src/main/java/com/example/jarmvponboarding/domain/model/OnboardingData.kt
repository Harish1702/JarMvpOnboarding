
package com.example.jarmvponboarding.domain.model

import androidx.compose.ui.graphics.Color

data class OnboardingData(
    val toolbarTitle: String,
    val introTitle: String,
    val introSubtitle: String,
    val cards: List<OnboardingCard>,
    val ctaLottie: String?,
    val bottomToCenterTranslationInterval: Long,
    val expandCardStayInterval: Long,
    val collapseCardTiltInterval: Long,
    val collapseExpandIntroInterval: Long
)

data class OnboardingCard(
    val imageUrl: String,
    val collapsedText: String,
    val expandedText: String,
    val backgroundColor: Color,
    val cardColor: Color,
    val strokeStartColor: Color,
    val strokeEndColor: Color,
    val startGradient: Color,
    val endGradient: Color
)