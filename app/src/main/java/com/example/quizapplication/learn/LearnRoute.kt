package com.example.quizapplication.learn

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapplication.learn.presentation.article.Article
import com.example.quizapplication.learn.presentation.article.ArticleListScreen
import com.example.quizapplication.learn.presentation.article.Section
import com.example.quizapplication.learn.presentation.article.SectionsList
import com.example.quizapplication.learn.presentation.articledetail.ArticleDetailScreen
import com.example.quizapplication.learn.presentation.subjects.SubjectsLearnScreen

enum class LearnModuleScreen {
    SUBJECTS, ARTICLE_LIST, ARTICLE_DETAIL
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LearnRoute(
    bottomBarVisibility: MutableState<Boolean>
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LearnModuleScreen.SUBJECTS.name
    ) {
        subjectsScreen(
            onSubjectSelected = { navController.navigateToArticleListScreen() },
            onTopicSelectedL = { navController.navigateToArticleDetailScreen() },
            bottomBarVisibility = bottomBarVisibility,
        )

        articleListScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToArticle = { navController.navigateToArticleDetailScreen() },
            bottomBarVisibility = bottomBarVisibility
        )

        articleDetailScreen(
            onNavigateToBack = { navController.popBackStack() },
            bottomBarVisibility = bottomBarVisibility,
        )
    }
}

fun NavGraphBuilder.subjectsScreen(
    onSubjectSelected: (subjectName: String) -> Unit,
    onTopicSelectedL: (topicId: Long) -> Unit,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = LearnModuleScreen.SUBJECTS.name
    ) {
        bottomBarVisibility.value = true

        SubjectsLearnScreen(
            onSubjectSelected = onSubjectSelected,
            onTopicSelected = onTopicSelectedL,
        )
    }
}

fun NavController.navigateToSubjectScreen() {
    this.navigate(LearnModuleScreen.SUBJECTS.name)
}

fun NavGraphBuilder.articleListScreen(
    onNavigateToArticle: (articleId: Long) -> Unit,
    onNavigateToBack: () -> Unit,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = LearnModuleScreen.ARTICLE_LIST.name
    ) {

        bottomBarVisibility.value = false

        ArticleListScreen(
            sectionsList = SectionsList(
                sections = listOf(
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                )
            ),
            onNavigateToArticle = onNavigateToArticle,
            onNavigateToBack = onNavigateToBack
        )
    }
}

fun NavController.navigateToArticleListScreen() {
    this.navigate(LearnModuleScreen.ARTICLE_LIST.name)
}

fun NavGraphBuilder.articleDetailScreen(
    onNavigateToBack: () -> Unit,
    bottomBarVisibility: MutableState<Boolean>,
) {
    composable(
        route = LearnModuleScreen.ARTICLE_DETAIL.name
    ) {
        bottomBarVisibility.value = false

        ArticleDetailScreen(
            onNavigateToBack = onNavigateToBack
        )
    }
}

fun NavController.navigateToArticleDetailScreen() {
    this.navigate(LearnModuleScreen.ARTICLE_DETAIL.name)
}