package com.example.quizapplication.quiz.di

import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.data.remote.OfflineQuizRemoteDataSourceImpl
import com.example.quizapplication.quiz.data.remote.QuizRemoteDataSource
import com.example.quizapplication.quiz.domain.QuizRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuizRepository(repositoryImpl: QuizRepositoryImpl) : QuizRepository

    @Binds
    @Singleton
    abstract fun bindQuizRemoteDataSource(source: OfflineQuizRemoteDataSourceImpl) : QuizRemoteDataSource
}