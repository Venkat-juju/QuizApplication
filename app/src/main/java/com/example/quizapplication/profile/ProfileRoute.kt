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
        profileScreen()
    }
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = ProfileScreens.PROFILE_SCREEN.name
    ) {
        ProfileScreen()
    }
}