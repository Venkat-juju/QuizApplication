package com.example.quizapplication.quiz.presentation.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    correctAnswers: Int,
    wrongAnswers: Int,
    skippedAnswers: Int,
    onShowAnswerClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    BackHandler {
        onExitClicked()
    }
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 24.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (wrongAnswers < correctAnswers) R.drawable.ic_check_circeld
                    else R.drawable.ic_circle_xmark
                ),
                contentDescription = "Result Icon",
                modifier = Modifier
                    .padding(12.dp)
                    .size(256.dp),
                tint = if (wrongAnswers < correctAnswers) Color.Green else Color.Red
            )

            Text(
                text = if(wrongAnswers < correctAnswers) "Yay! You nailed it 🎉"
                else "Don't Worry! Failures are the stepping stones 💪",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "RESULT",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    fontWeight = FontWeight.ExtraBold,
                )
                Divider(modifier = Modifier.padding(end = 8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Correct Questions:")
                    Text(text = correctAnswers.toString())
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Wrong Questions:")
                    Text(text = wrongAnswers.toString())
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Skipped Questions:")
                    Text(text = skippedAnswers.toString())
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Total Questions:")
                    Text(text = (correctAnswers + wrongAnswers + skippedAnswers).toString())
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { onShowAnswerClicked() }, modifier = Modifier.width(200.dp)) {
                    Text(text = "Review Answers")
                }

                OutlinedButton(onClick = onExitClicked, modifier = Modifier.width(200.dp)) {
                    Text("Exit")
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizResultPreview() {
    QuizApplicationTheme {
        QuizResultScreen(10, 5, 2, {}, {})
    }
}