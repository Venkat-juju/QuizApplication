package com.example.quizapplication.quiz.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapplication.quiz.data.QuizRepository
import com.example.quizapplication.quiz.util.NZResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    var state: HistoryUiState by mutableStateOf(HistoryUiState.Loading)
        private set
    init {
        fetchHistory()
    }

    fun onEvent(event: HistoryScreenEvents) {
        when (event) {
            is HistoryScreenEvents.DeleteHistory -> deleteAllHistory()
        }
    }

    private fun fetchHistory() {
        viewModelScope.launch {
            val historyResponse = repository.getAllHistories()

            state = when (historyResponse) {
                is NZResult.Success -> {
                    val histories = historyResponse.data
                    if (histories.isEmpty()) {
                        HistoryUiState.NoData
                    } else {
                        HistoryUiState.Success(history = histories)
                    }
                }
                is NZResult.Error -> HistoryUiState.Error
                is NZResult.Loading -> HistoryUiState.Loading
            }
        }
    }

    private fun deleteAllHistory() {
        viewModelScope.launch {
            val deleteHistoryResponse = repository.deleteAllHistory()

            when (deleteHistoryResponse) {
                is NZResult.Success -> {
                    state = HistoryUiState.NoData
                }
                is NZResult.Error -> {}
                is NZResult.Loading -> {}
            }
        }
    }
}