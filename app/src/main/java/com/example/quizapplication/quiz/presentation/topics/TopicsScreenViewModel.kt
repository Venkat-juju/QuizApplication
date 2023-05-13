package com.example.quizapplication.quiz.presentation.topics

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
class TopicsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizRepository: QuizRepository
): ViewModel() {

    private val subjectName: String = checkNotNull(savedStateHandle["subjectName"])
    private val type: Int = checkNotNull(savedStateHandle["type"])

    var state: TopicScreenUiState by mutableStateOf(TopicScreenUiState.Loading)
        private set

    init {
        when (type) {
            TopicScreenType.ALL.ordinal -> fetchTheTopics(subjectName)
            TopicScreenType.BOOKMARKS.ordinal -> fetchBookmarkTopics()
        }
    }

    private fun fetchTheTopics(subjectName: String) {
        viewModelScope.launch {

            val topics = quizRepository.getTopicsBySubject(subjectName)

            state = when (topics) {
                is NZResult.Success -> {
                    TopicScreenUiState.Success(
                        topics = topics.data,
                        type = TopicScreenType.ALL
                    )
                }

                is NZResult.Error -> {
                    TopicScreenUiState.Error(message = topics.message)
                }

                is NZResult.Loading -> {
                    TopicScreenUiState.Loading
                }
            }
        }
    }

    private fun fetchBookmarkTopics() {
        viewModelScope.launch {
            val bookmarkedTopics = quizRepository.getAllBookmarkedTopics()

            state = when (bookmarkedTopics) {
                is NZResult.Success -> {
                    if (bookmarkedTopics.data.isEmpty()) {
                        TopicScreenUiState.NoData(TopicScreenType.values()[type])
                    } else {
                        TopicScreenUiState.Success(
                            topics = bookmarkedTopics.data,
                            type = TopicScreenType.BOOKMARKS
                        )
                    }
                }

                is NZResult.Loading -> TopicScreenUiState.Loading

                is NZResult.Error -> {
                    TopicScreenUiState.Error(
                        message = "Topics fetching failed"
                    )
                }
            }
        }
    }
}