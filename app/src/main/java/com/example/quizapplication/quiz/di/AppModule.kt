package com.example.quizapplication.quiz.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.quizapplication.quiz.data.local.QuizDatabase
import com.example.quizapplication.quiz.data.local.dao.QuizDao
import com.example.quizapplication.quiz.data.remote.OfflineQuizRemoteDataSourceImpl
import com.example.quizapplication.quiz.data.remote.QuizRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideQuizRemoteDataSource(@ApplicationContext context: Context): QuizRemoteDataSource {
        return OfflineQuizRemoteDataSourceImpl(context)
    }
}