package com.example.quizapplication.quiz.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.quiz.presentation.compoenents.InitialIcon
import com.example.quizapplication.quiz.presentation.quiz.AlertDialog
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyState: HistoryUiState,
    onBackPressed: () -> Unit,
    onHistoryItemClicked: (Long, String) -> Unit,
    onHistoryDelete: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var showDeleteHistoryConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteHistoryConfirmationDialog) {
        AlertDialog(
            title = stringResource(id = R.string.confirm_delete),
            message = stringResource(id = R.string.confirm_delete_history_msg),
            positiveButtonText = stringResource(id = R.string.confirm),
            negativeButtonText = stringResource(id = R.string.cancel),
            onPositiveButtonClicked = {
                showDeleteHistoryConfirmationDialog = false
                onHistoryDelete()
            },
            onNegativeButtonClicked = { showDeleteHistoryConfirmationDialog = false }
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.history),
                        style = if (scrollBehavior.state.collapsedFraction < 0.5) {
                            MaterialTheme.typography.displayMedium
                        } else {
                            MaterialTheme.typography.titleLarge
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_press)
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { showDeleteHistoryConfirmationDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_history)
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { innerPadding ->
        when(historyState) {
            is HistoryUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HistoryUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(historyState.history.size) { index ->
                        HistoryListItem(
                            itemIndex=index+1,
                            historyState.history[index],
                            modifier = Modifier.clickable {
                                onHistoryItemClicked(
                                    historyState.history[index].historyId,
                                    historyState.history[index].historyTitle
                                )
                            }
                        )
                    }
                }
            }
            is HistoryUiState.NoData -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = stringResource(id = R.string.no_data))
                }
            }
            HistoryUiState.Error -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.something_went_wrong))
                }
            }
        }
    }
}

@Composable
fun HistoryListItem(
    itemIndex: Int,
    historyItem: QuizHistory,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            InitialIcon(
                name = "$itemIndex",
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(historyItem.historyTitle, style = MaterialTheme.typography.titleMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Stat(stringResource(id = R.string.total), historyItem.totalQuestions)
                    Stat(stringResource(id = R.string.correct), historyItem.correctAnswers)
                    Stat(stringResource(id = R.string.wrong), historyItem.wrongAnswers)
                    Stat(stringResource(id = R.string.skipped), historyItem.skippedAnswer)
                }
            }
        }
    }
}

@Composable
fun Stat(
    metricName: String,
    metricValue: Int
) {
    Column {
        Text(metricName, style = MaterialTheme.typography.titleSmall)
        Text(
            text = metricValue.toString()
        )
    }
}

@Preview
@Composable
fun HistoryItemPreview() {
    QuizApplicationTheme {
        HistoryListItem(
            itemIndex = 1,
            historyItem = QuizHistory(
                historyId = 123L,
                historyTitle = "title",
                totalQuestions = 10,
                correctAnswers = 7,
                wrongAnswers = 2,
                skippedAnswer = 1
            )
        )
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        historyState = HistoryUiState.Loading,
        onBackPressed = {},
        onHistoryItemClicked = { _, _ ->
        },
        onHistoryDelete = {
        }
    )
}