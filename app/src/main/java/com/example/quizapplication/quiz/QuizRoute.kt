package com.example.quizapplication.quiz

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.quizapplication.quiz.presentation.categories.SubjectsScreen
import com.example.quizapplication.quiz.presentation.categories.SubjectsScreenViewModel
import com.example.quizapplication.quiz.presentation.history.HistoryScreen
import com.example.quizapplication.quiz.presentation.history.HistoryScreenEvents
import com.example.quizapplication.quiz.presentation.history.HistoryScreenViewModel
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailViewModel
import com.example.quizapplication.quiz.presentation.topics.TopicsScreen
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailScreen
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailType
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailViewModelEvent
import com.example.quizapplication.quiz.presentation.quiz.QuizResultScreen
import com.example.quizapplication.quiz.presentation.topics.TopicScreenType
import com.example.quizapplication.quiz.presentation.topics.TopicsScreenViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

enum class QuizModuleScreen {
    QUIZ_DETAIL, QUIZ_RESULT, QUIZ_CATEGORIES, TOPIC_LIST, HISTORY
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizRoute() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = QuizModuleScreen.QUIZ_CATEGORIES.name
    ) {
        composable(
            route = QuizModuleScreen.QUIZ_CATEGORIES.name,
            enterTransition = {
                val initialStateDestination = initialState.destination.route
                when {
                    initialStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
                    initialStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    else -> null
                }
            },
            exitTransition =  {
                val targetStateDestination = targetState.destination.route

                when {
                    targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
                    targetStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    else -> null
                }
            }
        ) {
            val subjectsViewModel: SubjectsScreenViewModel = hiltViewModel()

            SubjectsScreen(
                state = subjectsViewModel.state,
                onSubjectSelected = { subject ->
                    navController.navigate(
                        route = "${QuizModuleScreen.TOPIC_LIST.name}/$subject/${TopicScreenType.ALL.ordinal}"
                    )
                },
                onBookmarksClicked = {
                    navController.navigate(
                        route = "${QuizModuleScreen.TOPIC_LIST.name}/Bookmarks/${TopicScreenType.BOOKMARKS.ordinal}"
                    )
                },
                onHistoryClicked = {
                    navController.navigate(QuizModuleScreen.HISTORY.name)
                }
            )
        }

        composable(
            route = "${QuizModuleScreen.TOPIC_LIST.name}/{subjectName}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                val initialStateDestination = initialState.destination.route
                when {
                    initialStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    initialStateDestination == QuizModuleScreen.QUIZ_CATEGORIES.name -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    initialStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    else -> null
                }
            },
            exitTransition = {
                val targetRoute = targetState.destination.route
                when {
                    targetRoute == QuizModuleScreen.QUIZ_CATEGORIES.name -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    targetRoute?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    else -> null
                }
            }
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName")

            val topicsViewModel: TopicsScreenViewModel = hiltViewModel()

            TopicsScreen(
                categoryName = subjectName ?: "",
                state = topicsViewModel.state,
                onTopicSelected = { type, numberOfQuestions, topics ->
                    navController.navigate(
                        route = "${QuizModuleScreen.QUIZ_DETAIL.name}?" +
                                "type=${type.ordinal}&" +
                                "numberOfQuestions=${numberOfQuestions}&" +
                                "topics=${topics.map { it.topicName }.joinToString(";")}&" +
                                "ids=${topics.map { it.topicId.toString() }.joinToString(";")}"
                    )
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${QuizModuleScreen.QUIZ_DETAIL.name}?type={type}&numberOfQuestions={numberOfQuestions}&topics={topics}&ids={ids}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("numberOfQuestions") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("topics") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("ids") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                val initialStateDestination = initialState.destination.route

                when {
                    initialStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
                    initialStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    initialStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    else -> null
                }
            },
            exitTransition = {
                val targetStateDestination = targetState.destination.route

                when {
                    targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
                    targetStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    targetStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    else -> null
                }
            }
        ) {
            val viewModel: QuizDetailViewModel = hiltViewModel()

            QuizDetailScreen(
                state = viewModel.state,
                onOptionSelected = { index, selectedOption ->
                    viewModel.onEvent(QuizDetailViewModelEvent.OnOptionSelected(index, selectedOption))
                },
                onBookmarkButtonClicked = { questionIndex ->
                    viewModel.onEvent(QuizDetailViewModelEvent.OnQuestionBookmarked(questionIndex))
                },
                onSubmit = { viewModel.onEvent(QuizDetailViewModelEvent.OnSubmit) },
                onSubmitted = { total, correct, wrong, historyId ->
                    navController.navigate("${QuizModuleScreen.QUIZ_RESULT.name}/$total,$correct,$wrong?id=$historyId")
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${QuizModuleScreen.QUIZ_RESULT.name}/{stats}?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ),
            enterTransition = {
                val initialStateDestination = initialState.destination.route
                when {
                    initialStateDestination?.contains("${QuizModuleScreen.QUIZ_DETAIL.name}?type=${QuizDetailType.RESULT_DETAIL.ordinal}") == true ->   {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    initialStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    else -> null
                }
            },
            exitTransition = {
                val targetStateDestination = targetState.destination.route
                when {
                    targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
                        targetStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    else -> null
                }
            }
        ) { backStackEntry ->

            val stats = backStackEntry.arguments?.getString("stats")?.split(",")?.map(String::toInt)
            val historyId = backStackEntry.arguments?.getLong("id")

            val totalQuestions = stats?.getOrNull(0) ?: 0
            val correctAnswers = stats?.getOrNull(1) ?: 0
            val wrongAnswers = stats?.getOrNull(2) ?: 0

            QuizResultScreen(
                correctAnswers = correctAnswers,
                wrongAnswers = wrongAnswers,
                skippedAnswers = totalQuestions - (correctAnswers + wrongAnswers),
                onShowAnswerClicked = {
                    navController.navigate(
                        route = "${QuizModuleScreen.QUIZ_DETAIL.name}?" +
                                    "type=${QuizDetailType.RESULT_DETAIL.ordinal}&ids=${historyId}"
                    )
                },
                onExitClicked = {
                    navController.popBackStack()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = QuizModuleScreen.HISTORY.name,
            enterTransition = {
                val initalStateDestination = initialState.destination.route

                when {
                    initalStateDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    initalStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    else -> null
                }
            },
            exitTransition = {
                val targetDestination = targetState.destination.route

                when {
                    targetDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
                    }
                    targetDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
                    }
                    else -> null
                }
            }
        ) {
            val viewModel: HistoryScreenViewModel = hiltViewModel()

            HistoryScreen(
                historyState = viewModel.state,
                onBackPressed = {
                    navController.popBackStack()
                },
                onHistoryItemClicked = { id, title ->
                    navController.navigate(
                        route = "${QuizModuleScreen.QUIZ_DETAIL.name}?" +
                                "type=${QuizDetailType.RESULT_DETAIL.ordinal}&ids=${id}&topics=${title}"
                    )
                },
                onHistoryDelete = { viewModel.onEvent(HistoryScreenEvents.DeleteHistory) }
            )
        }
    }
}