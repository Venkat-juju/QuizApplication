package com.example.quizapplication.quiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopicsEntity(
    @PrimaryKey
    val topicId: Long,
    val topicName: String,
    val subjectName: String,
    val numberOfQuestions: Int,
    val numberOfAttempts: Int
)