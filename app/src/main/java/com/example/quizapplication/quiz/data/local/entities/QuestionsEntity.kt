package com.example.quizapplication.quiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionsEntity(
    @PrimaryKey
    val questionId: Long,
    val topicId: Long,
    val questionStr: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctAnswer: String,
    val explanation: String? = null,
    val isAttempted: Boolean? = false,
    val isBookmarked: Boolean
)
