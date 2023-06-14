package com.example.quizapplication.quiz.data.remote

import com.example.quizapplication.quiz.data.remote.model.RemoteTopic
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.util.NZResult

interface QuizRemoteDataSource {

    suspend fun getAllTopics(): NZResult<List<RemoteTopic>>

    suspend fun getQuestionsByTopic(topicId: List<Long>, limit: Int): NZResult<List<Question>>

    suspend fun getRandomQuestions(numberOfQuestions: Int): NZResult<List<Question>>
}