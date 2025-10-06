package com.example.jarmvponboarding.data.remote.dto

import androidx.compose.ui.graphics.Color
import com.example.jarmvponboarding.domain.model.OnboardingCard
import com.example.jarmvponboarding.domain.model.OnboardingData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.core.graphics.toColorInt

@Serializable
data class OnboardingResponseDto(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: OnboardingDataDto
)

fun OnboardingResponseDto.toOnboardingData(): OnboardingData {
    return OnboardingData(
        toolbarTitle = this.data.manualBuyEducationData.toolBarText,
        cards = this.data.manualBuyEducationData.educationCardList.map { it.toOnboardingCard() },
        expandCardStayInterval = this.data.manualBuyEducationData.expandCardStayInterval,
        bottomToCenterTranslationInterval = this.data.manualBuyEducationData.bottomToCenterTranslationInterval,
        collapseCardTiltInterval = this.data.manualBuyEducationData.collapseCardTiltInterval,
        collapseExpandIntroInterval = this.data.manualBuyEducationData.collapseExpandIntroInterval,
        introTitle = this.data.manualBuyEducationData.introTitle,
        introSubtitle = this.data.manualBuyEducationData.introSubtitle,
        ctaLottie = this.data.manualBuyEducationData.ctaLottie
    )
}

@Serializable
data class OnboardingDataDto(
    @SerialName("manualBuyEducationData") val manualBuyEducationData: ManualBuyEducationDataDto
)

@Serializable
data class ManualBuyEducationDataDto(
    @SerialName("toolBarText") val toolBarText: String,
    @SerialName("educationCardList") val educationCardList: List<EducationCardDto>,
    @SerialName("expandCardStayInterval") val expandCardStayInterval: Long = 1400L,
    @SerialName("bottomToCenterTranslationInterval") val bottomToCenterTranslationInterval: Long = 800L,
    @SerialName("collapseCardTiltInterval") val collapseCardTiltInterval: Long = 600L,
    @SerialName("collapseExpandIntroInterval") val collapseExpandIntroInterval: Long = 600L,
    @SerialName("introTitle") val introTitle: String = "Welcome to",
    @SerialName("introSubtitle") val introSubtitle: String = "Onboarding",
    @SerialName("ctaLottie") val ctaLottie: String?
)

@Serializable
data class EducationCardDto(
    @SerialName("image") val image: String,
    @SerialName("collapsedStateText") val collapsedStateText: String,
    @SerialName("expandStateText") val expandStateText: String,
    @SerialName("backGroundColor") val backGroundColor: String,
    @SerialName("startGradient") val startGradient: String,
    @SerialName("strokeStartColor") val strokeStartColor: String,
    @SerialName("strokeEndColor") val strokeEndColor: String,
    @SerialName("endGradient") val endGradient: String,
)

fun EducationCardDto.toOnboardingCard(): OnboardingCard {
    return OnboardingCard(
        collapsedText = this.collapsedStateText,
        expandedText = this.expandStateText,
        imageUrl = this.image,
        backgroundColor = hexToColor(this.backGroundColor),
        cardColor = hexToColor(this.backGroundColor),
        strokeStartColor = hexToColor(this.strokeStartColor),
        strokeEndColor = hexToColor(this.strokeEndColor),
        startGradient = hexToColor(this.startGradient),
        endGradient = hexToColor(this.endGradient),

    )
}

private fun hexToColor(hex: String): Color {
    val colorString = if (hex.startsWith("#")) hex.substring(1) else hex
    return Color("#$colorString".toColorInt())
}