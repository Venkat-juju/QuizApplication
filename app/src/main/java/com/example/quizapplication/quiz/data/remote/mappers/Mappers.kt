package com.example.quizapplication.quiz.data.remote.mappers

import com.example.quizapplication.quiz.data.local.entities.QuestionsEntity
import com.example.quizapplication.quiz.data.local.entities.QuizHistoryEntity
import com.example.quizapplication.quiz.data.local.entities.TopicsEntity
import com.example.quizapplication.quiz.data.remote.model.RemoteTopic
import com.example.quizapplication.quiz.domain.model.Option
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.quiz.UiQuestion

fun RemoteTopic.toTopicEntity(): TopicsEntity {
    return TopicsEntity(
        topicId = this.topicId,
        topicName = this.topicName,
        subjectName = this.subjectName,
        numberOfQuestions = this.numberOfQuestions,
        numberOfAttempts = 0
    )
}

fun TopicsEntity.toTopic(): Topic {
    return Topic(
        topicId = this.topicId,
        topicName = this.topicName,
        numberOfQuestions = this.numberOfQuestions,
        numberOfAttempts = this.numberOfAttempts
    )
}

fun Question.toQuestionsEntity(): QuestionsEntity {
    return QuestionsEntity(
        questionId = this.questionId,
        topicId = this.topicId,
        questionStr = this.questionStr,
        option1 = this.options[0].optionStr,
        option2 = this.options[1].optionStr,
        option3 = this.options[2].optionStr,
        option4 = this.options[3].optionStr,
        correctAnswer = this.correctAnswer.optionStr,
        isBookmarked = this.isBookmarked,
        explanation = this.explanation
    )
}

fun QuestionsEntity.toQuestion(): Question {
    return Question(
        questionId = this.questionId,
        topicId = this.topicId,
        questionStr = this.questionStr,
        options = listOf(
            Option(this.option1),
            Option(this.option2),
            Option(this.option3),
            Option(this.option4)
        ),
        correctAnswer = Option(this.correctAnswer),
        explanation = this.explanation,
        isBookmarked = this.isBookmarked
    )
}

fun UiQuestion.toQuestion(): Question {
    return Question(
        questionId = this.questionId,
        questionStr = this.question,
        options = this.options.map{it.toOption()},
        correctAnswer = this.correctOption.toOption(),
        explanation = this.explanation,
        isBookmarked = this.isBookmarked,
        topicId = this.topicId,
        selectedOption = this.selectedOption?.toOption()
    )
}

fun String.toOption(): Option {
    return Option(this)
}