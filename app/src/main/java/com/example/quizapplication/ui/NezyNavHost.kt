package com.example.quizapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapplication.learn.LearnRoute
import com.example.quizapplication.profile.ProfileRoute
import com.example.quizapplication.quiz.QuizRoute

@Composable
fun NezyNavHost(
    navController: NavHostController,
    bottomBarVisibility: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = TopDestination.TEST.name
    ) {
        composable(route = TopDestination.LEARN.name) {
            LearnRoute(bottomBarVisibility = bottomBarVisibility)
        }
        composable(route = TopDestination.TEST.name) {
            QuizRoute(bottomBarVisibility = bottomBarVisibility)
        }
        composable(route = TopDestination.PROFILE.name) {
            ProfileRoute()
        }
    }
}