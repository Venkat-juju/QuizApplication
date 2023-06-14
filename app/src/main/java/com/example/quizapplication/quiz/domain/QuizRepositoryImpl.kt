package com.example.quizapplication.quiz.domain

import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.data.local.dao.QuizDao
import com.example.quizapplication.quiz.data.local.entities.QuestionsEntity
import com.example.quizapplication.quiz.data.local.entities.QuizHistoryEntity
import com.example.quizapplication.quiz.data.local.entities.TopicsEntity
import com.example.quizapplication.quiz.data.remote.QuizRemoteDataSource
import com.example.quizapplication.quiz.data.remote.mappers.toQuestion
import com.example.quizapplication.quiz.data.remote.mappers.toQuestionsEntity
import com.example.quizapplication.quiz.data.remote.mappers.toTopic
import com.example.quizapplication.quiz.data.remote.mappers.toTopicEntity
import com.example.quizapplication.quiz.data.remote.model.RemoteTopic
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.domain.model.Subject
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.history.QuizHistory
import com.example.quizapplication.quiz.util.NZResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource,
    private val localDataSource: QuizDao
) : QuizRepository {

    override suspend fun getAllSubjects(): NZResult<List<Subject>> {
        val cachedSubjects = localDataSource.getAllSubjects()

        if (cachedSubjects.isEmpty()) {
            val topicsFromRemote = remoteDataSource.getAllTopics()

            when(topicsFromRemote) {
                is NZResult.Success -> {
                    localDataSource.insertTopics(topicsFromRemote.data.map(RemoteTopic::toTopicEntity))
                    return NZResult.Success(
                        data = localDataSource.getAllSubjects().map { Subject(subjectName = it) }
                    )
                }
                is NZResult.Error -> {
                    return NZResult.Error(topicsFromRemote.message)
                }
                is NZResult.Loading -> Unit
            }
        }

        return NZResult.Success(data = cachedSubjects.map(String::toSubject))
    }

    override suspend fun getTopicsBySubject(subjectName: String): NZResult<List<Topic>> {
        val cachedTopics = localDataSource.getTopicsBySubjectName(subjectName = subjectName)

        if (cachedTopics.isEmpty()) {
            val topicsFromRemote = remoteDataSource.getAllTopics()

            when (topicsFromRemote) {
                is NZResult.Success -> {
                    localDataSource.insertTopics(
                        topicsFromRemote.data.map(RemoteTopic::toTopicEntity)
                    )

                    val topics = localDataSource.getTopicsBySubjectName(subjectName).map(TopicsEntity::toTopic)

                    return NZResult.Success(
                        data = topics
                    )
                }
                is NZResult.Error -> {
                    return NZResult.Error(topicsFromRemote.message)
                }
                is NZResult.Loading -> Unit
            }
        }

        val topics = cachedTopics.map(TopicsEntity::toTopic)
        topics.forEach {
            it.numberOfAttempts = localDataSource.getNumberOfQuestionsAttemptedByTopic(it.topicId)
        }

        return NZResult.Success(
            data = topics
        )
    }

    override suspend fun getQuestions(topicId: List<Long>, count: Int): NZResult<List<Question>> {

        if (topicId.isEmpty()) {
            return getRandomQuestions(count)
        }

        val cachedQuestions = localDataSource.getQuestionsByTopic(topicId, count)

        if (cachedQuestions.isEmpty() || cachedQuestions.size < count) {
            val questionsFromRemote = remoteDataSource.getQuestionsByTopic(topicId, count)

            when(questionsFromRemote) {
                is NZResult.Success -> {
                    localDataSource.insertQuestions(
                        questionsFromRemote.data.map(Question::toQuestionsEntity)
                    )

                    return NZResult.Success(
                        data = localDataSource.getQuestionsByTopic(topicId, count).map(QuestionsEntity::toQuestion)
                    )
                }
                is NZResult.Error -> {
                    return NZResult.Error(questionsFromRemote.message)
                }
                is NZResult.Loading -> Unit
            }
        }
        return NZResult.Success(data = cachedQuestions.map(QuestionsEntity::toQuestion))
    }

    override suspend fun saveQuizHistory(historyTitle: String, questions: List<Question>): NZResult<Long> {
        val historyId = (localDataSource.getLatestHistoryId() ?: 0L) + 1

        val questionsHistory = questions.map {
            QuizHistoryEntity(
                historyId = historyId,
                historyTitle = historyTitle,
                questionId = it.questionId,
                selectedOptionIndex = it.options.indexOf(it.selectedOption)
            )
        }

        localDataSource.insertHistory(questionsHistory)
        localDataSource.markQuestionsAsAttempted(
            questions.filter { it.selectedOption != null }.map(Question::questionId)
        )

        return NZResult.Success(data = historyId)
    }

    override suspend fun bookmarkQuestion(question: Question): NZResult<Boolean> {
        val questionsEntity = question.toQuestionsEntity()
        localDataSource.bookmarkQuestion(questionsEntity)

        return NZResult.Success(data = true)
    }

    override suspend fun getHistoryQuestions(historyId: Long): NZResult<List<Question>> {
        val historyQuestions = localDataSource.getHistoryQuestions(historyId)
            .map { Pair(it.questionId, it.selectedOptionIndex) }

        val questions = localDataSource.getQuestionsById(historyQuestions.map{ it.first }).map(QuestionsEntity::toQuestion)

        return NZResult.Success(
            data = questions.map { question ->
                val selectedOptionIndex = historyQuestions.find {
                    it.first == question.questionId
                }?.second
                selectedOptionIndex?.let {
                    if (selectedOptionIndex != -1) {
                        question.copy(selectedOption = question.options[selectedOptionIndex])
                    } else question
                } ?: question
            }
        )
    }

    override suspend fun getAllBookmarkedTopics(): NZResult<List<Topic>> {
        val allBookmarkedQuestions = localDataSource.getAllBookmarkedQuestions()
        val distinctTopicIds = allBookmarkedQuestions.map { it.topicId }.distinct()

        val bookmarkedTopics = localDataSource.getTopicsById(distinctTopicIds)
        val topics = bookmarkedTopics.map(TopicsEntity::toTopic).map {topic ->
            topic.copy(numberOfQuestions = allBookmarkedQuestions.count { it.topicId == topic.topicId })
        }

        return NZResult.Success(
            data = topics
        )
    }

    override suspend fun getBookmarksByTopic(topicId: Long): NZResult<List<Question>> {
        val bookmarkedQuestions = localDataSource.getBookmarksByTopic(topicId = topicId).map(QuestionsEntity::toQuestion)

        return NZResult.Success(
            data = bookmarkedQuestions
        )
    }

    override suspend fun getAllHistories(): NZResult<List<QuizHistory>> {
        val history = localDataSource.getAllHistory().groupBy(QuizHistoryEntity::historyId)

        val historyList: MutableList<QuizHistory> = mutableListOf()

        history.entries.forEach { historyItem ->
            val questions = localDataSource.getQuestionsById(
                historyItem.value.map(QuizHistoryEntity::questionId)
            )
            val topicsId = questions
                .distinctBy(QuestionsEntity::topicId)
                .map(QuestionsEntity::topicId)
            val topicsName = localDataSource
                .getTopicsById(topicsId)
                .map(TopicsEntity::topicName)

            val totalQuestions = historyItem.value.size
            val numberOfCorrectAnsweredQuestions = historyItem.value
                .filter { it.selectedOptionIndex != -1 }
                .filter { historyQuestion ->
                    questions.find { it.questionId == historyQuestion.questionId }?.let {
                        isCorrectAnswer(it, historyQuestion.selectedOptionIndex)
                    } ?: false
                }.size

            val numberOfSkippedQuestions = historyItem.value.filter { it.selectedOptionIndex == -1 }.size

            historyList.add(
                QuizHistory(
                    historyId = historyItem.key,
                    totalQuestions = historyItem.value.size,
                    historyTitle = historyItem.value.first().historyTitle,
                    correctAnswers = numberOfCorrectAnsweredQuestions,
                    wrongAnswers = totalQuestions - numberOfCorrectAnsweredQuestions - numberOfSkippedQuestions,
                    skippedAnswer = numberOfSkippedQuestions
                )
            )
        }

        historyList.sortByDescending(QuizHistory::historyId)

        return NZResult.Success(data = historyList)
    }

    override suspend fun deleteAllHistory(): NZResult<Unit> {
        localDataSource.deleteAllHistory()

        return NZResult.Success(data = Unit)
    }

    private fun isCorrectAnswer(question: QuestionsEntity, selectedOptionIndex: Int): Boolean {
        val correctAnswerIndex = question.let { question ->
            when (question.correctAnswer) {
                question.option1 -> 0
                question.option2 -> 1
                question.option3 -> 2
                question.option4 -> 3
                else -> -1
            }
        }

        return selectedOptionIndex == correctAnswerIndex
    }

    private suspend fun getRandomQuestions(numberOfQuestions: Int): NZResult<List<Question>> {
        val cachedQuestions = localDataSource.getRandomQuestions(numberOfQuestions)

        if (cachedQuestions.size < numberOfQuestions) {
            val remoteQuestions = remoteDataSource.getRandomQuestions(numberOfQuestions)

            when(remoteQuestions) {
                is NZResult.Success -> {
                    return NZResult.Success(
                        data = remoteQuestions.data
                    )
                }
                is NZResult.Error -> {
                    return NZResult.Error("Unable to fetch the questions")
                }
                is NZResult.Loading -> return NZResult.Loading<List<Question>>(true)
            }
        } else {
            return NZResult.Success(data = cachedQuestions.map(QuestionsEntity::toQuestion))
        }
    }
}

fun String.toSubject(): Subject {
    return Subject(this)
}