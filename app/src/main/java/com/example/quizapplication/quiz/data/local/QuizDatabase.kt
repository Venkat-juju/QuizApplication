package com.example.quizapplication.quiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quizapplication.quiz.data.local.dao.QuizDao
import com.example.quizapplication.quiz.data.local.entities.QuestionsEntity
import com.example.quizapplication.quiz.data.local.entities.QuizHistoryEntity
import com.example.quizapplication.quiz.data.local.entities.TopicsEntity

@Database(
    entities = [QuestionsEntity::class, TopicsEntity::class, QuizHistoryEntity::class],
    version = 1
)
abstract class QuizDatabase: RoomDatabase() {
    abstract val quizDao: QuizDao
}