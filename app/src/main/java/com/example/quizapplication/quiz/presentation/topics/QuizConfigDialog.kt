package com.example.quizapplication.quiz.presentation.topics

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailType
import com.example.quizapplication.ui.theme.QuizApplicationTheme
import com.google.accompanist.flowlayout.FlowRow

data class QuizConfig(
    val topics: List<String>,
    val type: QuizDetailType,
    val numberOfQuestions: Int
)

@Composable
fun QuizConfigDialog(
    topics: List<Topic>,
    totalQuestions: Int,
    onStart: (type: QuizDetailType, numberOfQuestions: Int, topics: List<Topic>) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var type: Int by rememberSaveable { mutableStateOf(0) }
    var numberOfQuestions: Int by rememberSaveable { mutableStateOf(totalQuestions / 5) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(
                onClick = {
                    if (numberOfQuestions == 0) {
                        Toast.makeText(context, "Number of questions can't be 0", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onStart(QuizDetailType.values()[type], numberOfQuestions, topics)
                }
            ) {
                Text("Start")
            }
         },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column {
                Text(
                    text = "Mode",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TabRow(
                    selectedTabIndex = type,
                    modifier = Modifier.padding(bottom = 16.dp),
                    indicator = {
                        QuizTypeTabIndicator(tabPositions = it, quizType = QuizDetailType.values()[type])
                    },
                    divider = {}
                ) {
                    QuizTypeTab(
                        title = "Practice",
                        onClick = { type = QuizDetailType.PRACTICE.ordinal }
                    )
                    QuizTypeTab(
                        title = "Test",
                        onClick = { type = QuizDetailType.TEST.ordinal }
                    )
                }
                Text(
                    text = "Topics",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TopicsChipGroup(topics = topics.map { it.topicName })
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    Text(text = "Number of questions:", style = MaterialTheme.typography.titleLarge)
                    Text(text = numberOfQuestions.toString(), style = MaterialTheme.typography.titleLarge)
                }
                Slider(
                    value = numberOfQuestions.toFloat(),
                    onValueChange = { numberOfQuestions = it.toInt() },
                    valueRange = 0f..totalQuestions.toFloat(),
                    steps = 4
                )
            }
        }
    )
}

@Composable
private fun QuizTypeTabIndicator(
    tabPositions: List<TabPosition>,
    quizType: QuizDetailType
) {
    val transition = updateTransition(
        quizType,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (QuizDetailType.PRACTICE isTransitioningTo QuizDetailType.TEST) {
                // Indicator moves to the right.
                // Low stiffness spring for the left edge so it moves slower than the right edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                // Indicator moves to the left.
                // Medium stiffness spring for the left edge so it moves faster than the right edge.
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (QuizDetailType.PRACTICE isTransitioningTo QuizDetailType.TEST) {
                // Indicator moves to the right
                // Medium stiffness spring for the right edge so it moves faster than the left edge.
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                // Indicator moves to the left.
                // Low stiffness spring for the right edge so it moves slower than the left edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun QuizTypeTab(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsChipGroup(topics: List<String>) {
    FlowRow(mainAxisSpacing = 8.dp) {
        topics.forEach { topic ->
            AssistChip(onClick = { /*TODO*/ }, label = { Text(text = topic, modifier = Modifier.padding(vertical = 8.dp)) })
        }
    }
}

@Preview
@Composable
fun QuizConfigScreenPreview() {
    QuizApplicationTheme {
        QuizConfigDialog(
            topics = listOf(
                Topic(1234L, "first topic", 12, 10),
                Topic(1244L, "second topic", 12, 10),
            ),
            totalQuestions = 100,
            onStart = {_,_,_ -> },
            onCancel = {}
        )
    }
}