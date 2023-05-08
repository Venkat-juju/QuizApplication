package com.example.quizapplication.quiz.data.remote

import com.example.quizapplication.quiz.data.remote.model.RemoteTopic
import com.example.quizapplication.quiz.domain.model.Option
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.util.NZResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineQuizRemoteDataSourceImpl @Inject constructor(): QuizRemoteDataSource {
    override suspend fun getAllTopics(): NZResult<List<RemoteTopic>> {
        delay(1000)
        return NZResult.Success(
            data = listOf(
                RemoteTopic(
                    1L,
                    "Physics",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    2L,
                    "Organic Chemistry",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    3L,
                    "Inorganic chemistry",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    4L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    5L,
                    "Botony",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    6L,
                    "Zoology",
                    subjectName = "Tamil",
                    100
                ),
                RemoteTopic(
                    7L,
                    "Physics",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    8L,
                    "Organic Chemistry",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    9L,
                    "Inorganic chemistry",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    10L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    11L,
                    "Botony",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    12L,
                    "Zoology",
                    subjectName = "English",
                    100
                ),
                RemoteTopic(
                    13L,
                    "Physics",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    14L,
                    "Organic Chemistry",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    15L,
                    "Inorganic chemistry",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    16L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    17L,
                    "Botony",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    18L,
                    "Zoology",
                    subjectName = "Maths",
                    100
                ),
                RemoteTopic(
                    19L,
                    "Physics",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    20L,
                    "Organic Chemistry",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    21L,
                    "Inorganic chemistry",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    22L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    23L,
                    "Botony",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    24L,
                    "Zoology",
                    subjectName = "Science",
                    100
                ),
                RemoteTopic(
                    25L,
                    "Physics",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    26L,
                    "Organic Chemistry",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    27L,
                    "Inorganic chemistry",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    28L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    29L,
                    "Botony",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    30L,
                    "Zoology",
                    subjectName = "Social",
                    100
                ),
                RemoteTopic(
                    31L,
                    "Physics",
                    subjectName = "General Knowledge",
                    100
                ),
                RemoteTopic(
                    32L,
                    "Organic Chemistry",
                    subjectName = "General Knowledge",
                    100
                ),
                RemoteTopic(
                    33L,
                    "Inorganic chemistry",
                    subjectName = "General Knowledge",
                    100
                ),
                RemoteTopic(
                    34L,
                    "This is very large title of this screen. this should come to next line",
                    subjectName = "General Knowledge",
                    100
                ),
                RemoteTopic(
                    35L,
                    "Botony",
                    subjectName = "General Knowledge",
                    100
                ),
                RemoteTopic(
                    36L,
                    "Zoology",
                    subjectName = "General Knowledge",
                    100
                ),
            )
        )
    }

    override suspend fun getQuestionsByTopic(topicId: List<Long>, limit: Int): NZResult<List<Question>> {
//        delay(1000)
        return NZResult.Success(
            data = listOf(
                Question(
                    questionId = "${topicId.first()}1".toLong(),
                    topicId = topicId.first(),
                    questionStr = "What is the difference between temperature and heat?",
                    options = listOf(
                        Option("Temperature is the energy transferred, while heat measures hotness or coldness"),
                        Option("Temperature measures hotness or coldness, while heat is the energy transferred due to a temperature difference"),
                        Option("Temperature and heat are the same thing"),
                        Option("None of the above")
                    ),
                    correctAnswer = Option("Temperature measures hotness or coldness, while heat is the energy transferred due to a temperature difference")
                ),
                Question(
                    questionId = "${topicId.first()}2".toLong(),
                    topicId = topicId.first(),
                    questionStr = "What are thermometers used for?",
                    options = listOf(
                        Option("Measuring heat"),
                        Option("Measuring temperature"),
                        Option("Measuring both heat and temperature"),
                        Option("None of the above")
                    ),
                    correctAnswer = Option("Measuring temperature"),
                ),
                Question(
                    questionId = "${topicId.first()}3".toLong(),
                    topicId = topicId.first(),
                    questionStr = "Which temperature scales are most commonly used?",
                    options = listOf(
                        Option("Fahrenheit, Celsius, and Kelvin"),
                        Option("Fahrenheit, Celsius, and Newton"),
                        Option("Celsius, Kelvin, and Newton"),
                        Option("Celsius, Fahrenheit, and Rankine")
                    ),
                    correctAnswer = Option("Fahrenheit, Celsius, and Kelvin"),
                ),
                Question(
                    questionId = "${topicId.first()}4".toLong(),
                    topicId = topicId.last(),
                    questionStr = "What is specific heat capacity?",
                    options = listOf(
                        Option("The amount of heat required to change the phase of a substance without a change in temperature"),
                        Option("The amount of heat required to raise the temperature of a substance by one degree Celsius or Kelvin per unit mass"),
                        Option("The transfer of heat through electromagnetic waves, such as infrared radiation"),
                        Option("None of the above")
                    ),
                    correctAnswer = Option("The amount of heat required to raise the temperature of a substance by one degree Celsius or Kelvin per unit mass")
                ),
                Question(
                    questionId = "${topicId.first()}5".toLong(),
                    topicId = topicId.last(),
                    questionStr = "What are the three methods of heat transfer?",
                    options = listOf(
                        Option("Conduction, convection, and reflection"),
                        Option("Conduction, convection, and absorption"),
                        Option("Conduction, convection, and radiation"),
                        Option("Conduction, convection, and refraction")
                    ),
                    correctAnswer = Option("Conduction, convection, and radiation")
                )
            )
        )
    }
}