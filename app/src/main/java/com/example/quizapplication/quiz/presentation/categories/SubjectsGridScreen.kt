package com.example.quizapplication.quiz.presentation.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Text(
                        "${state.earnedCoins}",
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = stringResource(id = R.string.coin),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
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
                HomePageContent(
                    state = state,
                    onBookmarksClicked = onBookmarksClicked,
                    onHistoryClicked = onHistoryClicked,
                    onStartDailyQuiz = onStartDailyQuiz,
                    onSubjectSelected = onSubjectSelected,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(4.dp)
                )
            }
            is SubjectsScreenUiState.Error -> Unit
        }
    }
}

@Composable
fun HomePageContent(
    state: SubjectsScreenUiState.Success,
    onBookmarksClicked: () -> Unit,
    onHistoryClicked: () -> Unit,
    onStartDailyQuiz: () -> Unit,
    onSubjectSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        SectionTitle(
            "Importants"
        )
        HistoryAndBookmarksCard(
            onHistoryClicked = onHistoryClicked,
            onBookmarksClicked = onBookmarksClicked,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )
        DailyQuizCard(
            onStart = onStartDailyQuiz,
            isCompleted = state.isDailyQuizCompleted,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        SectionTitle(
            stringResource(id = R.string.subjects)
        )
        SubjectsGrid(
            state = state,
            onSubjectSelected = onSubjectSelected
        )
        SectionTitle(
            stringResource(id = R.string.previous_year_questions)
        )
        PreviousYearQuestionsGrid()
        SectionTitle(titleString = "Connect With Us")
        SocialMedia(
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
fun SocialMedia(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        SMCard(
            mediaName = "Instagram",
            icon = painterResource(id = R.drawable.ic_instagram),
            modifier = Modifier.weight(1f)
        )
        SMCard(
            mediaName = "Whatsapp",
            icon = painterResource(id = R.drawable.ic_whatsapp),
            modifier = Modifier.weight(1f)
        )
        SMCard(
            mediaName = "Twitter",
            icon = painterResource(id = R.drawable.ic_twitter),
            modifier = Modifier.weight(1f)
        )
        SMCard(
            mediaName = "Facebook",
            icon = painterResource(id = R.drawable.ic_facebook),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SMCard(
    mediaName: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 12.dp)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                text = mediaName,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun PreviousYearQuestionsGrid() {
    LazyRow(
    ) {
        items(10) {number ->
            SubjectCard(
                subjectName = "namee",
                onSubjectSelected = {}
            )
        }
    }
}

@Composable
fun SectionTitle(titleString: String) {
    Text(
        text = titleString,
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .padding(start = 4.dp)
            .padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubjectsGrid(
    state: SubjectsScreenUiState.Success,
    onSubjectSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
    ) {
        repeat(
            state.subjects.size
        ) { index ->
            SubjectCard(
                subjectName = state.subjects[index],
                onSubjectSelected = onSubjectSelected
            )
        }
    }
}

@Composable
fun HistoryAndBookmarksCard(
    onHistoryClicked: () -> Unit,
    onBookmarksClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .height(75.dp),
        shape = RoundedCornerShape(4.dp),
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
}

@Composable
fun SubjectCard(
    subjectName: String,
    onSubjectSelected: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .height(150.dp)
            .width(190.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable {
                onSubjectSelected(subjectName)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id =
                    when (subjectName) {
                        "தமிழ்" -> R.drawable.ic_tamil_logo
                        "அறிவியல்" -> R.drawable.ic_science_logo
                        "சமூக அறிவியல்" -> R.drawable.ic_social_science_logo
                        else -> R.drawable.ic_gk_logo
                    }
                ),
                contentDescription = "Subject Icon",
                tint = Color.Unspecified
            )
            Text(
                text = subjectName,
                modifier = Modifier
                    .padding(top = 10.dp),
                style = MaterialTheme.typography.titleMedium
            )
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
                    tint = Color.Unspecified,
                    modifier = Modifier.size(80.dp)
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
fun HomePageContentPreview() {
    QuizApplicationTheme {
        HomePageContent(
            state = SubjectsScreenUiState.Success(
                listOf("தமிழ்", "அறிவியல்", "சமூக அறிவியல்", "GK")
            ),
            onBookmarksClicked = { /*TODO*/ },
            onHistoryClicked = { /*TODO*/ },
            onStartDailyQuiz = {},
            onSubjectSelected = {}
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

@Preview
@Composable
fun SMCardPreview() {
    QuizApplicationTheme {
        SMCard(
            mediaName = "Instagram",
            icon = painterResource(id = R.drawable.ic_instagram)
        )
    }
}