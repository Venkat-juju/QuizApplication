package com.example.quizapplication.quiz.data.datastore

import androidx.datastore.preferences.core.intPreferencesKey

object PreferenceKeys {
    val COINS_EARNED = intPreferencesKey("coins_earned")
    val DAILY_QUIZ_LAST_ATTENDED_DATE = intPreferencesKey("quiz_last_attended_date")
}