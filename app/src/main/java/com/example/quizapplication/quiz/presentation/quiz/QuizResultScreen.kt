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
import androidx.compose.ui.res.stringResource
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
    quizType: Int,
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
                text = if(wrongAnswers < correctAnswers) stringResource(id = R.string.success_message)
                else stringResource(id = R.string.failure_message),
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
                    text = stringResource(id = R.string.result_uppercase),
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
                    Text(text = stringResource(id = R.string.correct_answers))
                    Text(text = correctAnswers.toString())
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = stringResource(id = R.string.wrong_answers))
                    Text(text = wrongAnswers.toString())
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = stringResource(id = R.string.skipped_questions))
                    Text(text = skippedAnswers.toString())
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = stringResource(id = R.string.total_questions))
                    Text(text = (correctAnswers + wrongAnswers + skippedAnswers).toString())
                }
            }

            if (quizType == QuizDetailType.TEST.ordinal) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Coins Earned:    $correctAnswers")
                    Icon(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = stringResource(id = R.string.coin),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 4.dp)
                    )
                }
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { onShowAnswerClicked() }, modifier = Modifier.width(200.dp)) {
                    Text(text = stringResource(id = R.string.review_answers))
                }

                OutlinedButton(onClick = onExitClicked, modifier = Modifier.width(200.dp)) {
                    Text(stringResource(id = R.string.exit))
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizResultPreview() {
    QuizApplicationTheme {
        QuizResultScreen(
            10,
            5,
            2,
            quizType = 1,
            {},
            {}
        )
    }
}