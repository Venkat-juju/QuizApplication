package com.example.quizapplication.quiz

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.categories.SubjectsScreen
import com.example.quizapplication.quiz.presentation.categories.SubjectsScreenViewModel
import com.example.quizapplication.quiz.presentation.history.HistoryScreen
import com.example.quizapplication.quiz.presentation.history.HistoryScreenEvents
import com.example.quizapplication.quiz.presentation.history.HistoryScreenViewModel
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailScreen
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailType
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailViewModel
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailViewModelEvent
import com.example.quizapplication.quiz.presentation.quiz.QuizResultScreen
import com.example.quizapplication.quiz.presentation.topics.TopicScreenType
import com.example.quizapplication.quiz.presentation.topics.TopicsScreen
import com.example.quizapplication.quiz.presentation.topics.TopicsScreenViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

enum class QuizModuleScreen {
    QUIZ_DETAIL, QUIZ_RESULT, QUIZ_CATEGORIES, TOPIC_LIST, HISTORY
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizRoute(
    bottomBarVisibility: MutableState<Boolean>
) {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = QuizModuleScreen.QUIZ_CATEGORIES.name
    ) {

        subjectsScreen(
            onSubjectsSelected = { subjectName ->
                navController.navigateToTopicsScreen(subjectName, TopicScreenType.ALL)
            },
            onBookmarksClicked = {
                navController.navigateToTopicsScreen("Bookmarks", TopicScreenType.BOOKMARKS)
            },
            onHistoryClicked = navController::navigateToHistoryScreen,
            onStartDailyQuiz = {
                navController.navigateToQuizDetailScreen(QuizDetailType.TEST, numberOfQuestions = 10)
            },
            bottomBarVisibility = bottomBarVisibility
        )

        topicsScreen(
            onTopicSelected = { type, numberOfQuestions, topics ->
                navController.navigateToQuizDetailScreen(
                    type,
                    numberOfQuestions,
                    topics.map { it.topicName },
                    topics.map { it.topicId }
                )

            },
            onBackPressed = navController::popBackStack,
            bottomBarVisibility = bottomBarVisibility,
        )

        quizDetailScreen(
            onSubmit = { total, correctAnswer, wrongAnswers, historyId, quizType ->
                navController.navigateToQuizResultScreen(total, correctAnswer, wrongAnswers, historyId, quizType)
            },
            onBackPressed = navController::popBackStack,
            bottomBarVisibility = bottomBarVisibility,
        )

        quizResultScreen(
            onShowAnswerClicked = { historyId ->
                navController.navigateToQuizDetailScreen(QuizDetailType.RESULT_DETAIL, ids = listOf(historyId))
            },
            onExit = {
                // TODO: check some way to avoid this double popBackStack
                navController.popBackStack()
                navController.popBackStack()
            },
            bottomBarVisibility = bottomBarVisibility
        )

        historyScreen(
            onBackPressed = navController::popBackStack,
            onHistoryItemClicked = { id, title ->
                navController.navigateToQuizDetailScreen(
                    QuizDetailType.RESULT_DETAIL,
                    topics = listOf(title),
                    ids = listOf(id)
                )
            },
            bottomBarVisibility = bottomBarVisibility
        )
    }
}

//region: subjects screen
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.subjectsScreen(
    onSubjectsSelected: (subjectName: String) -> Unit,
    onBookmarksClicked: () -> Unit,
    onHistoryClicked: () -> Unit,
    onStartDailyQuiz: () -> Unit,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    bottomBarVisibility: MutableState<Boolean>,
) {

    composable(
        route = QuizModuleScreen.QUIZ_CATEGORIES.name,
        enterTransition = {enterTransition
//            val initialStateDestination = initialState.destination.route
//            when {
//                initialStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
//                        initialStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true ||
//                        initialStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true ||
//                        initialStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                else -> null
//            }
        },
        exitTransition =  { exitTransition
//            val targetStateDestination = targetState.destination.route
//
//            when {
//                targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                else -> null
//            }
        }
    ) {
        bottomBarVisibility.value = true

        val subjectsViewModel: SubjectsScreenViewModel = hiltViewModel()

        SubjectsScreen(
            state = subjectsViewModel.state,
            onSubjectSelected = onSubjectsSelected,
            onBookmarksClicked = onBookmarksClicked,
            onHistoryClicked = onHistoryClicked,
            onStartDailyQuiz = onStartDailyQuiz
        )
    }
}

//endregion

//region: topics screen

fun NavController.navigateToTopicsScreen(subjectName: String, type: TopicScreenType) {
    this.navigate(route = "${QuizModuleScreen.TOPIC_LIST.name}/$subjectName/${type.ordinal}")
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.topicsScreen(
    onTopicSelected: (type: QuizDetailType, numberOfQuestions: Int, topics: List<Topic>) -> Unit,
    onBackPressed: () -> Unit,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = "${QuizModuleScreen.TOPIC_LIST.name}/{subjectName}/{type}",
        arguments = listOf(
            navArgument("type") {
                type = NavType.IntType
            }
        ),
        enterTransition = { enterTransition
//            val initialStateDestination = initialState.destination.route
//            when {
//                initialStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                initialStateDestination == QuizModuleScreen.QUIZ_CATEGORIES.name -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                initialStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                else -> null
//            }
        },
        exitTransition = { exitTransition
//            val targetRoute = targetState.destination.route
//            when {
//                targetRoute == QuizModuleScreen.QUIZ_CATEGORIES.name -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                targetRoute?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                else -> null
//            }
        }
    ) { backStackEntry ->
        bottomBarVisibility.value = false
        val subjectName = backStackEntry.arguments?.getString("subjectName")

        val topicsViewModel: TopicsScreenViewModel = hiltViewModel()

        TopicsScreen(
            categoryName = subjectName ?: "",
            state = topicsViewModel.state,
            onTopicSelected = onTopicSelected,
            onBackPressed = onBackPressed,
        )
    }
}

//endregion

//region: quiz detail screen

fun NavController.navigateToQuizDetailScreen(
    type: QuizDetailType,
    numberOfQuestions: Int = -1,
    topics: List<String> = emptyList(),
    ids: List<Long> = emptyList(),
) {
    this.navigate(
        route = "${QuizModuleScreen.QUIZ_DETAIL.name}?" +
                "type=${type.ordinal}&" +
                "numberOfQuestions=$numberOfQuestions&" +
                "topics=${topics.joinToString(";")}&" +
                "ids=${ids.joinToString(";") { it.toString() }}"
    )
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.quizDetailScreen(
    onSubmit: (total: Int, correctAnswer: Int, wrongAnswers: Int, historyId: Long, quizType: Int) -> Unit,
    onBackPressed: () -> Unit,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    bottomBarVisibility: MutableState<Boolean>,
) {
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
        enterTransition = { enterTransition
//            val initialStateDestination = initialState.destination.route
//
//            when {
//                initialStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
//                        initialStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true ||
//                        initialStateDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                initialStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                else -> null
//            }
        },
        exitTransition = { exitTransition
//            val targetStateDestination = targetState.destination.route
//
//            when {
//                targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.HISTORY.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true-> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                targetStateDestination?.contains(QuizModuleScreen.QUIZ_RESULT.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                else -> null
//            }
        }
    ) {
        bottomBarVisibility.value = false
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
            onSubmitted = onSubmit,
            onBackClicked = onBackPressed
        )
    }
}

//endregion

//region: quiz result screen

fun NavController.navigateToQuizResultScreen(
    totalQuestions: Int,
    correctAnswer: Int,
    wrongAnswers: Int,
    historyId: Long,
    quizType: Int
) {
    this.navigate("${QuizModuleScreen.QUIZ_RESULT.name}/$totalQuestions,$correctAnswer,$wrongAnswers?id=$historyId&quizType=$quizType")
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.quizResultScreen(
    onShowAnswerClicked: (historyId: Long) -> Unit,
    onExit: () -> Unit,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = "${QuizModuleScreen.QUIZ_RESULT.name}/{stats}?id={id}&quizType={quizType}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            },
            navArgument("quizType") {
                type = NavType.IntType
                defaultValue = 0
            }
        ),
        enterTransition = { enterTransition
//            val initialStateDestination = initialState.destination.route
//            when {
//                initialStateDestination?.contains("${QuizModuleScreen.QUIZ_DETAIL.name}?type=${QuizDetailType.RESULT_DETAIL.ordinal}") == true ->   {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                initialStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                else -> null
//            }
        },
        exitTransition = { exitTransition
//            val targetStateDestination = targetState.destination.route
//            when {
//                targetStateDestination?.contains(QuizModuleScreen.TOPIC_LIST.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true ||
//                        targetStateDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                else -> null
//            }
        }
    ) { backStackEntry ->
        bottomBarVisibility.value = false

        val stats = backStackEntry.arguments?.getString("stats")?.split(",")?.map(String::toInt)
        val historyId = backStackEntry.arguments?.getLong("id")
        val quizType = backStackEntry.arguments?.getInt("quizType") ?: QuizDetailType.PRACTICE.ordinal

        val totalQuestions = stats?.getOrNull(0) ?: 0
        val correctAnswers = stats?.getOrNull(1) ?: 0
        val wrongAnswers = stats?.getOrNull(2) ?: 0

        QuizResultScreen(
            correctAnswers = correctAnswers,
            wrongAnswers = wrongAnswers,
            skippedAnswers = totalQuestions - (correctAnswers + wrongAnswers),
            quizType = quizType,
            onShowAnswerClicked = { historyId?.run { onShowAnswerClicked(historyId) } },
            onExitClicked = onExit,
        )
    }
}

//endregion

// region: history screen
fun NavController.navigateToHistoryScreen(
) {
    this.navigate(QuizModuleScreen.HISTORY.name)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.historyScreen(
    onBackPressed: () -> Unit,
    onHistoryItemClicked: (id: Long, title: String) -> Unit,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = QuizModuleScreen.HISTORY.name,
        enterTransition = { enterTransition
//            val initalStateDestination = initialState.destination.route
//
//            when {
//                initalStateDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                initalStateDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                else -> null
//            }
        },
        exitTransition = { exitTransition
//            val targetDestination = targetState.destination.route
//
//            when {
//                targetDestination?.contains(QuizModuleScreen.QUIZ_CATEGORIES.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
//                }
//                targetDestination?.contains(QuizModuleScreen.QUIZ_DETAIL.name) == true -> {
//                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
//                }
//                else -> null
//            }
        }
    ) {
        bottomBarVisibility.value = false
        val viewModel: HistoryScreenViewModel = hiltViewModel()

        HistoryScreen(
            historyState = viewModel.state,
            onBackPressed = onBackPressed,
            onHistoryItemClicked = onHistoryItemClicked,
            onHistoryDelete = { viewModel.onEvent(HistoryScreenEvents.DeleteHistory) }
        )
    }
}

// endregion