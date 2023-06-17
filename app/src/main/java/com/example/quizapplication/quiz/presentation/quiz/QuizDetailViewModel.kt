package com.example.quizapplication.quiz.presentation.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.data.datastore.QuizDataStore
import com.example.quizapplication.quiz.data.remote.mappers.toQuestion
import com.example.quizapplication.quiz.domain.model.Question
import com.example.quizapplication.quiz.util.NZResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizRepository: QuizRepository,
    private val quizDataStore: QuizDataStore
) : ViewModel() {

    private val type: QuizDetailType = QuizDetailType.values()[checkNotNull(savedStateHandle["type"])]
    private val topics: String = checkNotNull(savedStateHandle["topics"])
    private val numberOfQuestions: Int = checkNotNull(savedStateHandle["numberOfQuestions"])
    private val ids: String = checkNotNull(savedStateHandle["ids"])

    var state: QuizDetailScreenState by mutableStateOf(
        QuizDetailScreenState.Loading(
            title = if (type == QuizDetailType.RESULT_DETAIL) "Quiz Result" else if(topics.isBlank()) "Daily quiz" else topics.split(";").joinToString(","),
            quizType = type
        )
    )

    init {
        if (type == QuizDetailType.RESULT_DETAIL) {
            fetchRevealQuiz(ids.split(";").first().toLong())
        } else if (type == QuizDetailType.BOOKMARKS) {
            fetchBookmarkedQuestionsFromTopic(ids.split(";").first().toLong())
        } else {
            fetchTheQuestions(ids.split(";").filter { it.isNotBlank() }.map { it.toLong() }, numberOfQuestions)
        }
    }

    private fun fetchBookmarkedQuestionsFromTopic(topicId: Long) {
        viewModelScope.launch {
            val questions = quizRepository.getBookmarksByTopic(topicId)
            when (questions) {
                is NZResult.Success -> {
                    state = QuizDetailScreenState.Success(
                        questions = questions.data.map(Question::toUIQuestion),
                        title = state.quizTitle,
                        quizType = QuizDetailType.BOOKMARKS,
                    )
                }
                is NZResult.Loading -> {
                    state = QuizDetailScreenState.Loading(
                        title = state.quizTitle,
                        quizType = QuizDetailType.BOOKMARKS
                    )
                }
                is NZResult.Error -> {
                    state = QuizDetailScreenState.Error(
                        title = state.quizTitle,
                        quizType = QuizDetailType.BOOKMARKS,
                        errorMsg = "Unable to fetch the data"
                    )
                }
            }
        }
    }

    fun onEvent(event: QuizDetailViewModelEvent) {
        when(event) {
            is QuizDetailViewModelEvent.OnSubmit -> submitTheQuiz()
            is QuizDetailViewModelEvent.OnOptionSelected -> onOptionsSelected(event.questionIndex, event.selectedOption)
            is QuizDetailViewModelEvent.OnQuestionBookmarked -> bookmarkTheQuestion(event.questionIndex)
        }
    }

    private fun fetchTheQuestions(topicsIds: List<Long>, numberOfQuestions: Int) {
        viewModelScope.launch {
            val questionsResponse = quizRepository.getQuestions(topicsIds, numberOfQuestions)

            when(questionsResponse) {
                is NZResult.Success -> {
                    state = QuizDetailScreenState.Success(
                        questions = questionsResponse.data.map(Question::toUIQuestion),
                        title = state.quizTitle,
                        quizType = type
                    )
                }
                is NZResult.Error -> {
                    state = QuizDetailScreenState.Error(
                        title = state.quizTitle,
                        quizType = state.type,
                        errorMsg = "Failed to fetch data"
                    )
                }
                is NZResult.Loading -> {
                    state = QuizDetailScreenState.Loading(
                        title = state.quizTitle,
                        quizType = state.type
                    )
                }
            }
        }
    }

    private fun bookmarkTheQuestion(questionIndex: Int) {
        viewModelScope.launch {
            if (state is QuizDetailScreenState.Success) {
                var questionToBeBookmarked = (state as QuizDetailScreenState.Success).questions[questionIndex]
                questionToBeBookmarked = questionToBeBookmarked.copy(isBookmarked = !questionToBeBookmarked.isBookmarked)
                val question = questionToBeBookmarked.toQuestion()
                val bookMarkResponse = quizRepository.bookmarkQuestion(question)
                when(bookMarkResponse) {
                    is NZResult.Success -> {
                        state = (state as QuizDetailScreenState.Success).copy(
                            questions = (state as QuizDetailScreenState.Success).questions.mapIndexed { index, question ->
                                if (index == questionIndex) {
                                    question.copy(isBookmarked =  !question.isBookmarked)
                                } else
                                    question
                            }
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    // TODO:
    private fun onOptionsSelected(questionIndex: Int, selectedOption: String) {
        if (state.type == QuizDetailType.RESULT_DETAIL || state.type == QuizDetailType.PRACTICE && (state as? QuizDetailScreenState.Success)?.questions?.getOrNull(questionIndex)?.selectedOption?.isNullOrBlank() == false) {
            return
        }
        viewModelScope.launch {
            if (state is QuizDetailScreenState.Success) {
                state = (state as QuizDetailScreenState.Success)
                    .copy(
                        questions = (state as QuizDetailScreenState.Success).questions.mapIndexed { index, question ->
                            if (index == questionIndex) {
                                question.copy(selectedOption = selectedOption)
                            } else {
                                question
                            }
                        }
                    )
            }
        }
    }

    // TODO:
    private fun submitTheQuiz() {
        viewModelScope.launch {
            if (state is QuizDetailScreenState.Success) {
                val saveHistoryResponse = quizRepository.saveQuizHistory((state as QuizDetailScreenState.Success).title, (state as QuizDetailScreenState.Success).questions.map(UiQuestion::toQuestion))
                if (isDailyQuiz()) {
                    val timeNow = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("dd")
                    val todayDate = formatter.format(timeNow).toInt()

                    quizDataStore.updateDailyQuizAttendedDate(todayDate)
                }

                if (type == QuizDetailType.TEST) {
                    quizDataStore.addCoins(getTheNumberOfCorrectAnswers())
                }
                state = when(saveHistoryResponse) {
                    is NZResult.Success -> {
                        (state as QuizDetailScreenState.Success).copy(isSubmitting = true, historyId = saveHistoryResponse.data)
                    }

                    else -> { // TODO: handle failure cases
                        (state as QuizDetailScreenState.Success).copy(isSubmitting = true, historyId = -1L)
                    }
                }

            }
        }
    }

    private fun fetchRevealQuiz(quizId: Long) {
        viewModelScope.launch {
            val getHistoryResponse = quizRepository.getHistoryQuestions(quizId)

            when(getHistoryResponse) {
                is NZResult.Success -> {
                    state = QuizDetailScreenState.Success(
                        questions = getHistoryResponse.data.map(Question::toUIQuestion),
                        title = if (state.quizTitle.isNotBlank()) state.quizTitle else "Quiz Result",
                        quizType = QuizDetailType.RESULT_DETAIL,
                    )
                }
                is NZResult.Loading -> {
                    state = QuizDetailScreenState.Loading(
                        title = "Result",
                        quizType = QuizDetailType.RESULT_DETAIL
                    )
                }
                is NZResult.Error -> {
                    state = QuizDetailScreenState.Error(
                        title = "Result",
                        quizType = QuizDetailType.RESULT_DETAIL,
                        errorMsg = "Unable to fetch the result"
                    )
                }
            }
        }
    }

    private fun isDailyQuiz(): Boolean {
        return topics.isBlank()
    }

    private fun getTheNumberOfCorrectAnswers(): Int {
        if (state is QuizDetailScreenState.Success) {
            return  (state as QuizDetailScreenState.Success).questions.count {
                it.selectedOption == it.correctOption
            }
        }
        return 0
    }
}