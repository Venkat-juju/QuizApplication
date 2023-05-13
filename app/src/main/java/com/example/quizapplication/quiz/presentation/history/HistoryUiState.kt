package com.example.quizapplication.quiz.presentation.history

sealed interface HistoryUiState {
    object Loading : HistoryUiState

    data class Success(val history: List<QuizHistory>) : HistoryUiState

    object NoData : HistoryUiState

    object Error : HistoryUiState
}

data class QuizHistory(
    val historyId: Long,
    val historyTitle: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswer: Int
)
