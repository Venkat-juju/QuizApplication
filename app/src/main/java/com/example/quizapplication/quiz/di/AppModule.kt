package com.example.quizapplication.quiz.di

import android.app.Application
import androidx.room.Room
import com.example.quizapplication.quiz.data.local.QuizDatabase
import com.example.quizapplication.quiz.data.local.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(app: Application): QuizDatabase {
        return Room.databaseBuilder(
            app,
            QuizDatabase::class.java,
            "quizdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuizDao(db: QuizDatabase): QuizDao {
        return db.quizDao
    }
}