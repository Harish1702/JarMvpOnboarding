package com.example.jarmvponboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jarmvponboarding.domain.model.OnboardingCard
import com.example.jarmvponboarding.domain.model.OnboardingData
import com.example.jarmvponboarding.domain.usecase.GetOnboardingDataUseCase
import com.example.jarmvponboarding.presentation.OnboardingViewModel
import com.example.jarmvponboarding.presentation.mock.MockOnboardingRepositoryImpl
import com.example.jarmvponboarding.ui.theme.JarMvpOnboardingTheme
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreenWithMockData() {
    // Mock purpose only view model will be constructred ots
    val context = LocalContext.current
    val mockRepo = MockOnboardingRepositoryImpl(context)
    val useCase = GetOnboardingDataUseCase(mockRepo)
    val viewModel = OnboardingViewModel(useCase)
    JarMvpOnboardingTheme {
        OnboardingScreen(viewModel = viewModel, {})
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onNavigateToLanding: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    var animationState by remember { mutableStateOf(OnboardingAnimationState.Welcome) }
    var expandedIndex by remember { mutableIntStateOf(-1) }
    var runAnimation by remember { mutableStateOf(true) }

    val activeCardIndexDuringAnimation = when {
        animationState >= OnboardingAnimationState.TransitioningToCard3 -> 2
        animationState >= OnboardingAnimationState.TransitioningToCard2 -> 1
        animationState >= OnboardingAnimationState.Card1Entering -> 0
        else -> -1
    }

    val finalActiveIndex = if (animationState == OnboardingAnimationState.Complete) {
        expandedIndex
    } else {
        activeCardIndexDuringAnimation
    }

    val card = uiState.data?.cards?.getOrNull(finalActiveIndex)

    val startColor by animateColorAsState(
        targetValue = card?.startGradient ?: Color(0xFF201929),
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut),
        label = "StartColor"
    )
    val endColor by animateColorAsState(
        targetValue = card?.endGradient ?: Color(0xFF201929),
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut),
        label = "EndColor"
    )

    val backgroundBrush = Brush.verticalGradient(listOf(endColor, startColor))

    SharedTransitionLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    val headerAlpha by animateFloatAsState(
                        targetValue = if (animationState >= OnboardingAnimationState.HeaderVisible) 1f else 0f,
                        animationSpec = tween(durationMillis = 800, easing = EaseInOut),
                        label = "HeaderAlpha"
                    )
                    TopAppBar(
                        modifier = Modifier.graphicsLayer { alpha = headerAlpha },
                        title = { Text(uiState.data?.toolbarTitle ?: "", color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = {
                                runAnimation = true
                                expandedIndex = -1
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        uiState.isLoading -> CircularProgressIndicator(color = Color.White)
                        uiState.error != null -> Text(
                            text = uiState.error!!,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )

                        uiState.data != null -> {
                            WelcomeScreen(
                                onboardingData = uiState.data!!,
                                animationState = animationState
                            )

                            OnboardingContent(
                                onboardingData = uiState.data!!,
                                animationState = animationState,
                                onAnimationStateChange = { animationState = it },
                                expandedIndex = expandedIndex,
                                onCardClicked = { index ->
                                    expandedIndex = if (expandedIndex == index) -1 else index
                                },
                                onAutoExpand = { index -> expandedIndex = index },
                                onNavigateToLanding = onNavigateToLanding,
                                runAnimation = runAnimation,
                                onAnimationComplete = { runAnimation = false }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WelcomeScreen(onboardingData: OnboardingData, animationState: OnboardingAnimationState) {
    val welcomeAlpha by animateFloatAsState(
        targetValue = if (animationState == OnboardingAnimationState.Welcome) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        label = "WelcomeAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = welcomeAlpha },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = onboardingData.introTitle,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 24.sp
        )
        Text(
            text = onboardingData.introSubtitle,
            color = Color(0xFFF8DC83),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.OnboardingContent(
    onboardingData: OnboardingData,
    animationState: OnboardingAnimationState,
    onAnimationStateChange: (OnboardingAnimationState) -> Unit,
    expandedIndex: Int,
    onCardClicked: (Int) -> Unit,
    onAutoExpand: (Int) -> Unit,
    onNavigateToLanding: () -> Unit,
    runAnimation: Boolean,
    onAnimationComplete: () -> Unit
) {
    LaunchedEffect(onboardingData, runAnimation) {
        if (runAnimation) {
            onAnimationStateChange(OnboardingAnimationState.Welcome); delay(2000)
            onAnimationStateChange(OnboardingAnimationState.WelcomeFadingOut); delay(800)
            onAnimationStateChange(OnboardingAnimationState.HeaderVisible); delay(800)

            onAutoExpand(0); onAnimationStateChange(OnboardingAnimationState.Card1Entering); delay(
                onboardingData.bottomToCenterTranslationInterval
            )
            onAnimationStateChange(OnboardingAnimationState.Card1Expanded); delay(onboardingData.expandCardStayInterval)

            onAutoExpand(1); onAnimationStateChange(OnboardingAnimationState.TransitioningToCard2); delay(
                onboardingData.collapseCardTiltInterval
            )
            onAnimationStateChange(OnboardingAnimationState.Card2Entering); delay(onboardingData.collapseExpandIntroInterval)
            onAnimationStateChange(OnboardingAnimationState.Card2Expanded); delay(onboardingData.expandCardStayInterval)

            onAutoExpand(2); onAnimationStateChange(OnboardingAnimationState.TransitioningToCard3); delay(
                onboardingData.collapseCardTiltInterval
            )
            onAnimationStateChange(OnboardingAnimationState.Card3Entering); delay(onboardingData.collapseExpandIntroInterval)

            onAnimationStateChange(OnboardingAnimationState.Complete)
            onAnimationComplete()
        }
    }

    val card1OffsetY by animateFloatAsState(
        targetValue = if (animationState >= OnboardingAnimationState.Card1Entering) 0f else 1000f,
        animationSpec = tween(800, easing = EaseOutCubic),
        label = ""
    )
    val card2OffsetY by animateFloatAsState(
        targetValue = when {
            animationState == OnboardingAnimationState.TransitioningToCard2 -> 400f
            animationState >= OnboardingAnimationState.Card2Entering -> 0f
            else -> 1000f
        }, animationSpec = tween(800, easing = EaseOutCubic), label = ""
    )
    val card3OffsetY by animateFloatAsState(
        targetValue = when {
            animationState == OnboardingAnimationState.TransitioningToCard3 -> 400f
            animationState >= OnboardingAnimationState.Card3Entering -> 0f
            else -> 1000f
        }, animationSpec = tween(800, easing = EaseOutCubic), label = ""
    )


    val card1Rotation by animateFloatAsState(
        targetValue = if (animationState == OnboardingAnimationState.TransitioningToCard2) 6f else 0f,
        animationSpec = tween(600, easing = EaseOut),
        label = ""
    )
    val card2Rotation by animateFloatAsState(
        targetValue = when (animationState) {
            OnboardingAnimationState.TransitioningToCard2 -> -6f
            OnboardingAnimationState.TransitioningToCard3 -> -6f
            else -> 0f
        }, animationSpec = tween(600, easing = EaseOut), label = ""
    )
    val card3Rotation by animateFloatAsState(
        targetValue = if (animationState == OnboardingAnimationState.TransitioningToCard3) 6f else 0f,
        animationSpec = tween(600, easing = EaseOut),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (animationState.ordinal>2){ // if we are not in the welcome screens
            Box(
                modifier = Modifier
                    .size(453.84.dp)
                    .offset(y = (LocalConfiguration.current.screenHeightDp  / 1.5 ).dp) // Move down by half height so center is below screen
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x50FFDBF6),
                                Color(0x00FFDBF6)
                            ),
                        ),
                        shape = CircleShape
                    )
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val cards = onboardingData.cards
            cards.getOrNull(0)?.takeIf { animationState >= OnboardingAnimationState.Card1Entering }
                ?.let {
                    OnboardingItem(
                        it,
                        0 == expandedIndex,
                        { onCardClicked(0) },
                        Modifier.graphicsLayer {
                            translationY = card1OffsetY; rotationZ = card1Rotation
                        })
                }
            cards.getOrNull(1)
                ?.takeIf { animationState >= OnboardingAnimationState.TransitioningToCard2 }
                ?.let {
                    Spacer(Modifier.height(16.dp))
                    OnboardingItem(
                        it,
                        1 == expandedIndex,
                        { onCardClicked(1) },
                        Modifier.graphicsLayer {
                            translationY = card2OffsetY; rotationZ = card2Rotation
                        })
                }
            cards.getOrNull(2)
                ?.takeIf { animationState >= OnboardingAnimationState.TransitioningToCard3 }
                ?.let {
                    Spacer(Modifier.height(16.dp))
                    OnboardingItem(
                        it,
                        2 == expandedIndex,
                        { onCardClicked(2) },
                        Modifier.graphicsLayer {
                            translationY = card3OffsetY; rotationZ = card3Rotation
                        })
                }

        }
        AnimatedVisibility(
            visible = animationState == OnboardingAnimationState.Complete,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            onboardingData.ctaLottie?.let {
                SaveInGoldButton(
                    onClick = onNavigateToLanding,
                    lottieUrl = it
                )
            }
        }
    }
}

@Composable
fun SaveInGoldButton(onClick: () -> Unit, lottieUrl: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieUrl))
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(31.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF272239)),
        modifier = Modifier
            .height(48.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Save in Gold",
                color = Color(0xFFFDF3D6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 24.dp)
            )
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp),
                iterations = LottieConstants.IterateForever
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.OnboardingItem(
    card: OnboardingCard,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = animateColorAsState(
                 Color.DarkGray.copy(
                    alpha = 0.3f
                ), tween(600, easing = EaseOut), label = ""
            ).value
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(listOf(card.strokeStartColor, card.strokeEndColor))
        )
    ) {
        AnimatedContent(
            isExpanded,
            transitionSpec = { fadeIn(tween(600)) togetherWith fadeOut(tween(150)) },
            label = ""
        ) { target ->
            if (target) ExpandedContent(card, this) else CollapsedContent(card, this)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExpandedContent(card: OnboardingCard, scope: AnimatedVisibilityScope) {
    Column(
        Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current).data(card.imageUrl).crossfade(true).build(),
            card.expandedText,
            Modifier
                .fillMaxWidth()
                .size(296.dp, 340.dp)
                .sharedElement(rememberSharedContentState("image/${card.imageUrl}"), scope)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            card.expandedText,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.sharedElement(
                rememberSharedContentState("text/${card.collapsedText}"),
                scope
            )
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CollapsedContent(
    card: OnboardingCard,
    scope: AnimatedVisibilityScope
) {
    Row(
        Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current).data(card.imageUrl).crossfade(true).build(),
            null,
            Modifier
                .size(40.dp)
                .sharedElement(rememberSharedContentState("image/${card.imageUrl}"), scope)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            card.collapsedText,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .sharedElement(rememberSharedContentState("text/${card.collapsedText}"), scope)
        )
        Icon(
            Icons.Default.KeyboardArrowDown,
            "Expand",
            tint = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A2E)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreenWithMockData()
}