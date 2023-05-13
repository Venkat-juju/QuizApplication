package com.example.quizapplication.quiz.presentation.categories

sealed interface SubjectsScreenUiState {
    object Loading : SubjectsScreenUiState
    data class Success(val subjects: List<String>) : SubjectsScreenUiState
    data class Error(val errorMsg: String) : SubjectsScreenUiState
}