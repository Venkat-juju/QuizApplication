package com.example.quizapplication.quiz.presentation.topics

import com.example.quizapplication.quiz.domain.model.Topic

sealed interface TopicScreenUiState {
    object Loading: TopicScreenUiState
    data class Success(val topics: List<Topic>, val type: TopicScreenType): TopicScreenUiState
    data class Error(val message: String): TopicScreenUiState
    data class NoData(val type: TopicScreenType): TopicScreenUiState
}

enum class TopicScreenType {
    ALL, BOOKMARKS
}