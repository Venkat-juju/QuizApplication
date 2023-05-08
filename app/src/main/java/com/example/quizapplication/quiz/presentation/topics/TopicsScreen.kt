package com.example.quizapplication.quiz.presentation.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.quiz.domain.model.Topic
import com.example.quizapplication.quiz.presentation.compoenents.InitialIcon
import com.example.quizapplication.quiz.presentation.quiz.QuizDetailType
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    categoryName: String,
    state: TopicScreenUiState,
    onTopicSelected: (QuizDetailType, Int, List<Topic>) -> Unit,
    onBackPressed: () -> Unit,
) {

    var configTopic: Topic? = null

    var showConfigDialog by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = categoryName,
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
                            contentDescription = "Press Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor= MaterialTheme.colorScheme.onPrimary,
                ),
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) { innerPadding ->
        when(state) {
            is TopicScreenUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
            is TopicScreenUiState.Success -> {
                if (showConfigDialog) {
                    configTopic?.let { topic ->
                        QuizConfigDialog(
                            topics = listOf(topic),
                            totalQuestions = topic.numberOfQuestions,
                            onStart = { type, numberOfQuestions, topics ->
                                showConfigDialog = false
                                onTopicSelected(type, numberOfQuestions, topics)
                            },
                            onCancel = { showConfigDialog = false }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(state.topics.size) { topicIndex ->
                        TopicItem(
                            topic = state.topics[topicIndex],
                            type = state.type,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (state.type == TopicScreenType.BOOKMARKS) {
                                        onTopicSelected(
                                            QuizDetailType.BOOKMARKS,
                                            state.topics[topicIndex].numberOfQuestions,
                                            listOf(state.topics[topicIndex])
                                        )
                                    } else {
                                        configTopic = state.topics[topicIndex]
                                        showConfigDialog = true
                                    }
                                }
                        )
                    }
                }
            }
            is TopicScreenUiState.Error -> {
                Box {
                    Text("Something went wrong")
                }
            }
            is TopicScreenUiState.NoData -> {
                NoDataScreen(type = state.type)
            }
        }
    }
}

@Composable
fun TopicItem(
    topic: Topic,
    type: TopicScreenType,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            InitialIcon(name = topic.topicName)
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = topic.topicName,
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(text = "Total Questions", style = MaterialTheme.typography.titleSmall)
                        Text(text = topic.numberOfQuestions.toString())
                    }
                    if (type != TopicScreenType.BOOKMARKS) {
                        Column {
                            Text(text = "Attempted", style = MaterialTheme.typography.titleSmall)
                            Text(text = topic.numberOfAttempts.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoDataScreen(type: TopicScreenType) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bookmark_outlined),
            contentDescription = "Bookmark icon",
            modifier = Modifier
                .size(75.dp)
                .border(border = BorderStroke(2.dp, Color.Black), shape = CircleShape)
                .padding(8.dp)
        )
        Text(
            text = if (type == TopicScreenType.BOOKMARKS) "No Bookmarks found" else "No Topics Found",
            modifier = Modifier.padding(top = 12.dp),
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview
@Composable
fun NoDataScreenPreview() {
    QuizApplicationTheme {
        NoDataScreen(TopicScreenType.BOOKMARKS)
    }
}

@Preview
@Composable
fun TopicItemPreview() {
    QuizApplicationTheme {
        TopicItem(
            topic = Topic(
                topicId = 1L,
                topicName = "Sample Topic",
                numberOfQuestions = 100,
                numberOfAttempts = 10
            ),
            type = TopicScreenType.ALL
        )
    }
}

@Preview
@Composable
fun QuizTopicScreenPreview() {
    QuizApplicationTheme {
        TopicsScreen(
            categoryName = "Tamil",
            state = TopicScreenUiState.Success(
                topics = listOf(
                    Topic(
                        topicId = 1L,
                        topicName = "Sample Topic",
                        numberOfQuestions = 100,
                        numberOfAttempts = 10
                    ),
                    Topic(
                        topicId = 1L,
                        topicName = "Sample Topic",
                        numberOfQuestions = 100,
                        numberOfAttempts = 10
                    ),
                    Topic(
                        topicId = 1L,
                        topicName = "Sample Topic",
                        numberOfQuestions = 100,
                        numberOfAttempts = 10
                    ),
                    Topic(
                        topicId = 1L,
                        topicName = "Sample Topic",
                        numberOfQuestions = 100,
                        numberOfAttempts = 10
                    )
                ),
                type = TopicScreenType.ALL
            ),
            onTopicSelected = { _, _, _ ->

            },
            onBackPressed = {

            }
        )
    }
}