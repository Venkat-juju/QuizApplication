package com.example.quizapplication.quiz.presentation.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.util.NZResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: QuizRepository
) : ViewModel() {

    var state: SubjectsScreenUiState by mutableStateOf(SubjectsScreenUiState.Loading)
        private set

    init {
        fetchAllSubjects()
    }

    private fun fetchAllSubjects() {
        viewModelScope.launch {
            val subjects = repository.getAllSubjects()
            state = when (subjects) {
                is NZResult.Success -> {
                    SubjectsScreenUiState.Success(
                        subjects = subjects.data.map{ it.subjectName }
                    )
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

            /*state = SubjectsScreenUiState.Success(
                subjects = listOf(
                    "Tamil",
                    "English",
                    "Maths",
                    "Science",
                    "Social",
                    "General Knowledge",
                )
            )*/
        }
    }
}