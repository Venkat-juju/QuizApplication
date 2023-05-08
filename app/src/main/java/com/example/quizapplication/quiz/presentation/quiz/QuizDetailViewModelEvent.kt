package com.example.quizapplication.quiz.presentation.quiz

sealed interface QuizDetailViewModelEvent {
    data class OnOptionSelected(val questionIndex: Int, val selectedOption: String):
        QuizDetailViewModelEvent
    data class OnQuestionBookmarked(val questionIndex: Int): QuizDetailViewModelEvent
    object OnSubmit: QuizDetailViewModelEvent
}