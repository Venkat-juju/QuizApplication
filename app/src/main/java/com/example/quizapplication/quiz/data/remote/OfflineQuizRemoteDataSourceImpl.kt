package com.example.quizapplication.quiz.data.remote

import android.content.Context
import com.example.quizapplication.quiz.data.remote.model.RemoteTopic
import com.example.quizapplication.quiz.data.util.readJSONFromAsset
import com.example.quizapplication.quiz.domain.model.Option
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.util.NZResult
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

const val TOPICS_FILE_NAME = "topics.json"
const val TAMIL_QUESTIONS_FILE_NAME = "tamil.json"

@Singleton
class OfflineQuizRemoteDataSourceImpl @Inject constructor(
    val appContext: Context
): QuizRemoteDataSource {
    override suspend fun getAllTopics(): NZResult<List<RemoteTopic>> {
        val topics = mutableListOf<RemoteTopic>()
        val jsons = appContext.readJSONFromAsset(TOPICS_FILE_NAME)
        val topicsJson: JSONArray = jsons?.first()?.get("topics") as JSONArray
        for(i in 0 until topicsJson.length()) {
            val topic = topicsJson[i] as JSONObject
            topics.add(
                RemoteTopic(
                    topicId = topic.getInt("id").toLong(),
                    topicName = topic.getString("topicName").trim(),
                    subjectName = topic.getString("subjectName").trim(),
                    numberOfQuestions = topic.getInt("numberOfQuestions")
                )
            )
        }
        return NZResult.Success(
            data = topics
        )
    }

    override suspend fun getQuestionsByTopic(topicId: List<Long>, limit: Int): NZResult<List<Question>> {
        val questions = mutableListOf<Question>()
        val jsons = appContext.readJSONFromAsset(TAMIL_QUESTIONS_FILE_NAME)
        val questionsJson = jsons?.first()?.get("questions") as JSONArray
        for(i in 0 until questionsJson.length()) {
            val currentQuestion = questionsJson[i] as JSONObject
            val currentTopicId = currentQuestion.getInt("topicId").toLong()
            if (topicId.contains(currentTopicId)) {
                var explanation: String? = null
                try {
                    explanation = currentQuestion.getString("explanation").trim()
                } catch (e: Exception) {}
                questions.add(
                    Question(
                        questionId = currentQuestion.getInt("questionId").toLong(),
                        topicId = currentTopicId,
                        options = listOf(
                            Option(currentQuestion.getString("option1").trim()),
                            Option(currentQuestion.getString("option2").trim()),
                            Option(currentQuestion.getString("option3").trim()),
                            Option(currentQuestion.getString("option4").trim())
                        ),
                        correctAnswer = Option(currentQuestion.getString("answer").trim()),
                        questionStr = currentQuestion.getString("question").trim(),
                        explanation = explanation
                    )
                )
            }
        }
        return NZResult.Success(
            data = questions
        )
    }

    override suspend fun getRandomQuestions(numberOfQuestions: Int): NZResult<List<Question>> {
        val questions = mutableListOf<Question>()
        val jsons = appContext.readJSONFromAsset(TAMIL_QUESTIONS_FILE_NAME)
        val questionsJson = jsons?.first()?.get("questions") as JSONArray
        repeat(10) { index ->
            val currentQuestion = questionsJson[index] as JSONObject
            val currentTopicId = currentQuestion.getInt("topicId").toLong()
            var explanation: String? = null
            try {
                explanation = currentQuestion.getString("explanation").trim()
            } catch (e: Exception) {}
            questions.add(
                Question(
                    questionId = currentQuestion.getInt("questionId").toLong(),
                    topicId = currentTopicId,
                    options = listOf(
                        Option(currentQuestion.getString("option1").trim()),
                        Option(currentQuestion.getString("option2").trim()),
                        Option(currentQuestion.getString("option3").trim()),
                        Option(currentQuestion.getString("option4").trim())
                    ),
                    correctAnswer = Option(currentQuestion.getString("answer").trim()),
                    questionStr = currentQuestion.getString("question").trim(),
                    explanation = explanation
                )
            )
        }

        return NZResult.Success(
            data = questions
        )
    }
}