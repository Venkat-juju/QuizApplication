package com.example.quizapplication.quiz.util

sealed interface NZResult<T> {
    data class Success<T>(val data: T): NZResult<T>
    data class Error<T>(val message: String): NZResult<T>
    data class Loading<T>(val isLoading: Boolean): NZResult<T>
}