package com.example.quizapplication.quiz.domain.model

data class Question(
    val questionId: Long,
    val topicId: Long,
    val questionStr: String,
    val options: List<Option>,
    val correctAnswer: Option,
    val explanation: String? = null,
    val isBookmarked: Boolean = false,
    val selectedOption: Option? = null,
)

data class Option(
    val optionStr: String
)