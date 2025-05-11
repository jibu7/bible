package com.example.biblereader.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry

@ExperimentalAnimationApi
object BibleAnimations {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(300)) +
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(300)
        )
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(300)
        )
    }

    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(300)) +
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(300)
        )
    }

    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300)
        )
    }
}

@Composable
fun AnimatedVerseCard(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        content = content
    )
}

@Composable
fun AnimatedBookmarkIcon(
    isBookmarked: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isBookmarked,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}

@Composable
fun AnimatedSearchBar(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        content = content
    )
}

@Composable
fun AnimatedHighlight(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        exit = shrinkVertically() + fadeOut(),
        content = content
    )
} 