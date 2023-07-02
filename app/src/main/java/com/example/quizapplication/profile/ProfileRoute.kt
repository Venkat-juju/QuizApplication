package com.example.quizapplication.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapplication.profile.presentation.ProfileScreen

enum class ProfileScreens {
    PROFILE_SCREEN
}

@Composable
fun ProfileRoute() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ProfileScreens.PROFILE_SCREEN.name
    ) {
        profileScreen(
            onNavigateToBookmarks = {
                // TODO: this needs to be moved from here. this is profile module and this screen is quiz module.
            },
            onNavigateToHistory = {
                // TODO: this needs ot be moved from here. this is profile module and this screen is quiz module.
            }
        )
    }
}

fun NavGraphBuilder.profileScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
) {
    composable(
        route = ProfileScreens.PROFILE_SCREEN.name
    ) {
        ProfileScreen(
            onNavigateToBookmarks = onNavigateToBookmarks,
            onNavigateToHistory = onNavigateToHistory,
        )
    }
}