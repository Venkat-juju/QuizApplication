package com.example.quizapplication.quiz.presentation.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapplication.R
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    state: SubjectsScreenUiState,
    onSubjectSelected: (String) -> Unit,
    onBookmarksClicked: () -> Unit,
    onHistoryClicked: () -> Unit,
    onStartDailyQuiz: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.subject),
                        style = if (scrollBehavior.state.collapsedFraction < 0.5) {
                            MaterialTheme.typography.displayMedium
                        } else {
                            MaterialTheme.typography.titleLarge
                        }
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) { innerPadding ->
        when (state) {
            is SubjectsScreenUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SubjectsScreenUiState.Success -> {
                SubjectsGrid(
                    state = state,
                    onBookmarksClicked = onBookmarksClicked,
                    onHistoryClicked = onHistoryClicked,
                    onSubjectSelected = onSubjectSelected,
                    onStartDailyQuiz = onStartDailyQuiz,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is SubjectsScreenUiState.Error -> Unit
        }
    }
}

@Composable
fun SubjectsGrid(
    state: SubjectsScreenUiState.Success,
    onBookmarksClicked: () -> Unit,
    onHistoryClicked: () -> Unit,
    onSubjectSelected: (String) -> Unit,
    onStartDailyQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(
            span = { index ->
                if (index <= 1) {
                    GridItemSpan(maxLineSpan)
                } else {
                    GridItemSpan(1)
                }
            },
            count = state.subjects.size + 2
        ) { index ->
            if (index == 0) {
                ElevatedCard(
                    modifier = Modifier
                        .height(75.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                                .clickable { onBookmarksClicked() }
                                .padding(start = 12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark_colored),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                stringResource(id = R.string.bookmarks),
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(2.dp)
                                .padding(vertical = 12.dp)
                                .clip(RoundedCornerShape(50))
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                                .clickable { onHistoryClicked() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_history_colored),
                                contentDescription = "History Icon",
                                modifier = Modifier.padding(start = 12.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = stringResource(id = R.string.history),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            } else if (index == 1) {
                DailyQuizCard(
                    onStart = onStartDailyQuiz,
                    isCompleted = state.isDailyQuizCompleted,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                ElevatedCard(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .clickable {
                            onSubjectSelected(state.subjects[index - 1])
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id =
                                when {
                                    state.subjects[index-2] == "தமிழ்" -> R.drawable.ic_tamil_logo
                                    state.subjects[index-2] == "அறிவியல்" -> R.drawable.ic_science_logo
                                    state.subjects[index-2] == "சமூக அறிவியல்" -> R.drawable.ic_social_science_logo
                                    else -> R.drawable.ic_gk_logo
                                }
                            ),
                            contentDescription = "History Icon",
                            tint = Color.Unspecified
                        )
                        Text(
                            text = state.subjects[index - 2],
                            modifier = Modifier
                                .padding(top = 10.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyQuizCard(
    onStart: () -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(start = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_test),
                    contentDescription = "Daily quiz logo",
                    tint = Color.Unspecified
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.daily_quiz_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(text = stringResource(id = R.string.daily_quiz_number_of_questions))
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isCompleted) {
                        Button(onClick = onStart) {
                            Text(stringResource(id = R.string.start))
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            tint = Color.Green,
                            contentDescription = stringResource(id = R.string.completed_check_mark),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.completed_with_exclamatory),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DailyQuizCardPreview() {
    QuizApplicationTheme {
        DailyQuizCard(
            onStart = {},
            isCompleted = true
        )
    }
}

@Preview
@Composable
fun SubjectsScreenPreview() {
    QuizApplicationTheme {
        SubjectsScreen(
            state = SubjectsScreenUiState.Success(
                listOf("தமிழ்", "அறிவியல்", "சமூக அறிவியல்", "GK")
            ),
            onSubjectSelected = {},
            onBookmarksClicked = {},
            onHistoryClicked = {},
            onStartDailyQuiz = {}
        )
    }
}