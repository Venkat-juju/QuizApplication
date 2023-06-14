package com.example.quizapplication.quiz.presentation.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.data.datastore.QuizDataStore
import com.example.quizapplication.quiz.util.NZResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SubjectsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: QuizRepository,
    private val quizDataStore: QuizDataStore
) : ViewModel() {

    var state: SubjectsScreenUiState by mutableStateOf(SubjectsScreenUiState.Loading)
        private set

    private var isDailyQuizCompleted: Boolean = false

    init {
        fetchAllSubjects()
        updateDailyQuizStatus()
    }

    private fun fetchAllSubjects() {
        viewModelScope.launch {
            val subjects = repository.getAllSubjects()
            state = when (subjects) {
                is NZResult.Success -> {
                    if (isDailyQuizCompleted) {
                        SubjectsScreenUiState.Success(
                            subjects = subjects.data.map{ it.subjectName },
                            isDailyQuizCompleted = true
                        )
                    } else {
                        SubjectsScreenUiState.Success(
                            subjects = subjects.data.map{ it.subjectName }
                        )
                    }
                }
                is NZResult.Loading -> {
                    SubjectsScreenUiState.Loading
                }
                is NZResult.Error -> {
                    SubjectsScreenUiState.Error(
                        errorMsg = subjects.message
                    )
                }
            }
        }
    }

    private fun updateDailyQuizStatus() {
        viewModelScope.launch {
            quizDataStore.dailyQuizLastAttendedDateFlow.collect { dailyQuizLastAttendedDate ->
                val timeNow = Calendar.getInstance().time
                val formatter = SimpleDateFormat("dd")
                val todayDate = formatter.format(timeNow).toInt()

                if (todayDate == dailyQuizLastAttendedDate) {
                    if (state is SubjectsScreenUiState.Success) {
                        state = (state as SubjectsScreenUiState.Success).copy(isDailyQuizCompleted = true)
                    } else {
                        isDailyQuizCompleted = true
                    }
                }
            }
        }
    }
}