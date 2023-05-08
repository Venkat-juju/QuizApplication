package com.example.quizapplication.quiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val historyId: Long = 0L,
    val questionId: Long,
    val selectedOptionIndex: Int,
)
