package com.example.quizapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.quizapplication.quiz.QuizRoute
import com.example.quizapplication.ui.theme.QuizApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

// TODO: change primary color to #10316b 
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizApplicationTheme {
                // A surface container using the 'background' color from the theme
                QuizRoute()
            }
        }
    }
}

