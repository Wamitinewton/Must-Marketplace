package com.newton.mustmarket.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.composable

object NavigationTransitions {
    // Standard durations following Material Design guidelines
    private const val NAVIGATION_DURATION = 300
    private const val FADE_DURATION = 250

    // Horizontal sliding animation for primary navigation
    fun horizontalSlide(): NavTransition = NavTransition(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = NAVIGATION_DURATION,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = NAVIGATION_DURATION,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = NAVIGATION_DURATION,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = NAVIGATION_DURATION,
                    easing = FastOutSlowInEasing
                )
            )
        }
    )

    // Vertical sliding animation for modal-like screens
    fun verticalSlide(): NavTransition = NavTransition(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(NAVIGATION_DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(NAVIGATION_DURATION)
            )
        }
    )

    // Fade animation for overlay screens or dialogs
    fun fade(): NavTransition = NavTransition(
        enterTransition = {
            fadeIn(animationSpec = tween(FADE_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(FADE_DURATION))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(FADE_DURATION))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(FADE_DURATION))
        }
    )
}

// Data class to hold all transitions for a route
data class NavTransition(
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
)

// Extension function for easier navigation setup
fun androidx.navigation.NavGraphBuilder.composableWithAnimations(
    route: String,
    navTransition: NavTransition = NavigationTransitions.horizontalSlide(),
    arguments: List<androidx.navigation.NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = navTransition.enterTransition,
        exitTransition = navTransition.exitTransition,
        popEnterTransition = navTransition.popEnterTransition,
        popExitTransition = navTransition.popExitTransition,
        content = content
    )
}