package com.example.quizapplication.quiz.presentation.categories

sealed class SubjectsScreenUiState(val earnedCoins: Int = 0) {
    data class Loading(val numberOfEarnedCoins: Int = 0) : SubjectsScreenUiState(numberOfEarnedCoins)
    data class Success(
        val subjects: List<String>,
        val isDailyQuizCompleted: Boolean = false,
        val numberOfEarnedCoins: Int = 0
    ) : SubjectsScreenUiState(numberOfEarnedCoins)
    data class Error(
        val errorMsg: String,
        val numberOfEarnedCoins: Int = 0
    ) : SubjectsScreenUiState(numberOfEarnedCoins)
}