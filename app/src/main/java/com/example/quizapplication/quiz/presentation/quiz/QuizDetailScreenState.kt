package com.example.quizapplication.quiz.presentation.quiz

import com.example.quizapplication.quiz.domain.model.Question

sealed class QuizDetailScreenState(val quizTitle: String, val type: QuizDetailType) {

    data class Loading(
        val title: String,
        val quizType: QuizDetailType
    ) : QuizDetailScreenState(title, quizType)

    data class Success(
        val questions: List<UiQuestion>,
        val title: String,
        val quizType: QuizDetailType,
        val isSubmitting: Boolean = false,
        val result: QuizResult? = null,
        val historyId: Long? = null
    ) : QuizDetailScreenState(quizTitle = title, quizType)

    data class Error(
        val title: String,
        val quizType: QuizDetailType,
        val errorMsg: String
    ) : QuizDetailScreenState(title, quizType)
}

data class UiQuestion(
    val questionId: Long,
    val topicId: Long,
    val question: String,
    val options: List<String>,
    val correctOption: String,
    val explanation: String?,
    val selectedOption: String? = null,
    val isBookmarked: Boolean = false,
)

enum class QuizDetailType {
    PRACTICE,
    TEST,
    RESULT_DETAIL,
    BOOKMARKS
}

data class QuizResult(
    val correctAnswer: Int,
    val skippedAnswer: Int,
    val wrongAnswer: Int,
)

fun Question.toUIQuestion(): UiQuestion {
    return UiQuestion(
        question = this.questionStr,
        options = this.options.map { it.optionStr },
        correctOption = this.correctAnswer.optionStr,
        explanation = this.explanation,
        selectedOption = this.selectedOption?.optionStr,
        isBookmarked = this.isBookmarked,
        topicId = this.topicId,
        questionId = this.questionId
    )
}