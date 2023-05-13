package com.example.quizapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
