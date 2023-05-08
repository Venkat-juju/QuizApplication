package com.example.quizapplication.quiz.domain.model

data class Topic(
    val topicId: Long,
    val topicName: String,
    val numberOfQuestions: Int,
    var numberOfAttempts: Int = 0
)
