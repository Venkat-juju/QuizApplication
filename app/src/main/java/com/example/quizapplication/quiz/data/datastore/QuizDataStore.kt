package com.example.quizapplication.quiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val dailyQuizLastAttendedDateFlow: Flow<Int> = dataStore.data.catch {exception ->
        emit(emptyPreferences())
    }.map { preferences ->
        preferences[PreferenceKeys.DAILY_QUIZ_LAST_ATTENDED_DATE] ?: -1
    }

    suspend fun updateDailyQuizAttendedDate(date: Int) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.DAILY_QUIZ_LAST_ATTENDED_DATE] = date
            }
        } catch (e: Exception) {}
    }
}