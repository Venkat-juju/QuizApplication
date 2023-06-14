package com.example.quizapplication.quiz.data

import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.domain.model.Subject
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.history.QuizHistory
import com.example.quizapplication.quiz.util.NZResult

interface QuizRepository {

    suspend fun getAllSubjects(): NZResult<List<Subject>>

    suspend fun getTopicsBySubject(subjectName: String): NZResult<List<Topic>>

    suspend fun getQuestions(topicId: List<Long>, count: Int): NZResult<List<Question>>

    suspend fun saveQuizHistory(historyTitle: String, questions: List<Question>): NZResult<Long>

    suspend fun bookmarkQuestion(question: Question): NZResult<Boolean>

    suspend fun getHistoryQuestions(historyId: Long): NZResult<List<Question>>

    suspend fun getAllBookmarkedTopics(): NZResult<List<Topic>>

    suspend fun getBookmarksByTopic(topicId: Long): NZResult<List<Question>>

    suspend fun getAllHistories(): NZResult<List<QuizHistory>>

    suspend fun deleteAllHistory(): NZResult<Unit>
}