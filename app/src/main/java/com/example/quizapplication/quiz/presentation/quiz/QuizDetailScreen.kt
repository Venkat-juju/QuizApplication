package com.example.quizapplication.quiz.presentation.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(
    state: QuizDetailScreenState,
    onOptionSelected: (Int, String) -> Unit,
    onBookmarkButtonClicked: (Int) -> Unit,
    onSubmit: () -> Unit,
    onSubmitted: (Int, Int, Int, Long) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showSubmitDialog by rememberSaveable { mutableStateOf(false) }
    var showSubmitLoader: Boolean by rememberSaveable { mutableStateOf(false) }
    var showExitConfirmationDialog: Boolean by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (state.type == QuizDetailType.BOOKMARKS || state.type == QuizDetailType.RESULT_DETAIL) {
            onBackClicked()
        } else {
            showExitConfirmationDialog = true
        }
    }

    if (state is QuizDetailScreenState.Success && state.isSubmitting) {
        LaunchedEffect(key1 = Unit) {
            showSubmitLoader = false
            onSubmitted(
                state.questions.size,
                state.questions.filter { it.selectedOption == it.correctOption }.size,
                state.questions.filter {
                    !it.selectedOption.isNullOrBlank() && it.selectedOption != it.correctOption
                }.size,
                state.historyId ?: -1L
            )
        }
    }

    if (showExitConfirmationDialog) {
        AlertDialog(
            title = "Confirm Exit",
            message = "Are you sure to exit?",
            positiveButtonText = "Leave",
            negativeButtonText = "No, Stay",
            onPositiveButtonClicked = {
                showExitConfirmationDialog = false
                onBackClicked()
            },
            onNegativeButtonClicked = {
                showExitConfirmationDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(text = state.quizTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.type == QuizDetailType.RESULT_DETAIL ||
                                state.type == QuizDetailType.BOOKMARKS
                            ) {
                                onBackClicked()
                            } else {
                                showExitConfirmationDialog = true
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back press")
                    }
                },
                actions = {
                    if (
                        state.type != QuizDetailType.RESULT_DETAIL &&
                        state.type != QuizDetailType.BOOKMARKS
                    ) {
                        Button(
                            onClick = {
                                showSubmitDialog = true
                            },
                            enabled = state is QuizDetailScreenState.Success
                        ) {
                            Text("Submit")
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (state) {
                is QuizDetailScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is QuizDetailScreenState.Success -> {
                    if (showSubmitDialog) {
                        AlertDialog(
                            title = "Confirm Submit",
                            message = "Do you want to really submit the test?",
                            positiveButtonText = "Yes",
                            negativeButtonText = "No",
                            onPositiveButtonClicked = {
                                showSubmitDialog = false
                                showSubmitLoader = true
                                onSubmit()
                            },
                            onNegativeButtonClicked = {
                                showSubmitDialog = false
                            }
                        )
                    }
                    if (showSubmitLoader) {
                        LoadingDialog(loadingMessage = "Submitting...")
                    }
                    NZLinearProgressIndicator(
                        indicatorProgress = (state.questions.filter { it.selectedOption != null }.size.toFloat() / state.questions.size)
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        itemsIndexed(
                            items = state.questions,
                            key = { index, _ -> index }
                        ) { index, quizQuestion ->
                            QuestionDetail(
                                question = quizQuestion.question,
                                options = quizQuestion.options,
                                isBookmarked = quizQuestion.isBookmarked,
                                selectedOption = quizQuestion.selectedOption,
                                explanation = quizQuestion.explanation,
                                onOptionSelected = { selectedOption ->
                                    onOptionSelected(index, selectedOption)
                                },
                                questionIndex = index,
                                type = state.type,
                                onBookmarkButtonClicked = onBookmarkButtonClicked,
                                correctAnswer = quizQuestion.correctOption
                            )
                        }
                    }
                }
                is QuizDetailScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.errorMsg)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionDetail(
    questionIndex: Int,
    question: String,
    options: List<String>,
    isBookmarked: Boolean = false,
    selectedOption: String?,
    correctAnswer: String,
    explanation: String?,
    type: QuizDetailType,
    onOptionSelected: (String) -> Unit,
    onBookmarkButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showExplanation: Boolean by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                Text(text = "Question $questionIndex", fontWeight = FontWeight.Bold)
                Row {
                    AnimatedContent(
                        targetState = isBookmarked,
                        transitionSpec = { scaleIn() with scaleOut() },
                        label = ""
                    ) {
                        IconButton(onClick = { onBookmarkButtonClicked(questionIndex) }) {
                            Icon(
                                painter = painterResource(
                                    id = if (isBookmarked)R.drawable.ic_bookmark_filled
                                        else R.drawable.ic_bookmark_outlined
                                ),
                                contentDescription = "Bookmark"
                            )
                        }
                    }
                }
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Text(text = question, modifier = Modifier.padding(12.dp))
            options.forEach { optionStr ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (type == QuizDetailType.TEST && selectedOption == optionStr)
                                    Color.Blue
                                else if ((type == QuizDetailType.PRACTICE && !selectedOption.isNullOrBlank() && optionStr == correctAnswer) || ((type == QuizDetailType.RESULT_DETAIL || type == QuizDetailType.BOOKMARKS) && optionStr == correctAnswer))
                                    Color.Green
                                else if ((type == QuizDetailType.PRACTICE || type == QuizDetailType.RESULT_DETAIL) && !selectedOption.isNullOrBlank() && selectedOption == optionStr)
                                    Color.Red
                                else
                                    MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .selectable(optionStr == selectedOption) {
                            onOptionSelected(optionStr)
                        }
                        .background (
                            color = if (type == QuizDetailType.TEST && selectedOption == optionStr)
                                Color.Blue.copy(alpha = 0.1f)
                            else if (
                                (type == QuizDetailType.PRACTICE && !selectedOption.isNullOrBlank() && optionStr == correctAnswer) ||
                                ((type == QuizDetailType.RESULT_DETAIL || type == QuizDetailType.BOOKMARKS) && optionStr == correctAnswer)
                            )
                                Color.Green.copy(alpha = 0.1f)
                            else if (
                                (type == QuizDetailType.PRACTICE || type == QuizDetailType.RESULT_DETAIL) &&
                                !selectedOption.isNullOrBlank() && selectedOption == optionStr
                            )
                                Color.Red.copy(alpha = 0.1f)
                            else
                                Color.Transparent
                        )
                        .padding(horizontal = 12.dp, vertical = 12.dp)

                ) {
                    if (type == QuizDetailType.TEST) {
                        RadioButton(
                            selected = optionStr == selectedOption,
                            onClick = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else if (
                        (type == QuizDetailType.PRACTICE && !selectedOption.isNullOrBlank() && optionStr == correctAnswer) ||
                        ((type == QuizDetailType.RESULT_DETAIL || type == QuizDetailType.BOOKMARKS) && optionStr == correctAnswer)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            "correct answer",
                            tint = Color.Green,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else if (
                        (type == QuizDetailType.PRACTICE || type == QuizDetailType.RESULT_DETAIL) &&
                        !selectedOption.isNullOrBlank() && selectedOption == optionStr
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            "wrong answer",
                            tint = Color.Red,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else {
                        RadioButton(
                            selected = false,
                            onClick = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Text(
                        text = optionStr,
                        modifier = Modifier
                            .padding(end = 12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                visible = type == QuizDetailType.RESULT_DETAIL ||
                        (type == QuizDetailType.PRACTICE && !selectedOption.isNullOrBlank())
            ) {
              Divider(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 8.dp)
              )
                Column {
                    Text(
                        text = if (showExplanation) "Hide Explanation" else "Show Explanation",
                        modifier = Modifier
                            .clickable { showExplanation = !showExplanation }
                            .padding(12.dp)
                    )
                    AnimatedVisibility(visible = showExplanation) {
                        Text(
                            text = if (explanation.isNullOrBlank()) "Sorry! No explanation available" else explanation,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(onClick = onPositiveButtonClicked) {
                Text(text = positiveButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onNegativeButtonClicked) {
                Text(text = negativeButtonText)
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        }
    )
}

@Composable
fun LoadingDialog(loadingMessage: String) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Text(text = loadingMessage, modifier = Modifier.padding(start = 12.dp))
            }
        }
    )
}

@Composable
fun NZLinearProgressIndicator(indicatorProgress: Float) {
    var progress by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1500
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)), // Rounded edges
        progress = progressAnimation,
        color = Color.Green
    )
    LaunchedEffect(indicatorProgress) {
        progress = indicatorProgress
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    QuizApplicationTheme {
        AlertDialog(
            title = "Confirm",
            message = "Do you confirm?",
            positiveButtonText = "Ok",
            negativeButtonText = "Cancel",
            onPositiveButtonClicked = { /*TODO*/ }) {

        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview() {
    QuizApplicationTheme {
        LoadingDialog(loadingMessage = "Loading...")
    }
}

@Preview
@Composable
fun QuestionDetailPreview() {
    var selectedOption: String? = null
    QuizApplicationTheme {
        QuestionDetail(
            question = "This is sample question?",
            options = listOf(
                "This is Option1",
                "This is the longer option in the list of options. " +
                "This could be much longer than you expect. This is to check how it behaves " +
                "for longer text options.",
                "Option3"
            ),
            selectedOption = selectedOption,
            correctAnswer = "",
            explanation = "",
            onOptionSelected = { option ->
                selectedOption = option
            },
            type = QuizDetailType.PRACTICE,
            questionIndex = 1,
            onBookmarkButtonClicked = {}
        )
    }
}
