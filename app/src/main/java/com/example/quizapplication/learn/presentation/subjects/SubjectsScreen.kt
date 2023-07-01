package com.example.quizapplication.learn.presentation.subjects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsLearnScreen(
    onSubjectSelected: (subjectName: String) -> Unit,
    onTopicSelected: (topicId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Learn") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = "Recent Notes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                Divider()
            }
            item {
                RecentArticles(
                    onTopicSelected = onTopicSelected,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            item {
                Text(
                    text = "Subjects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                Divider()
            }
            items(50) {
                SubjectsCard(
                    modifier = Modifier.clickable { onSubjectSelected("asdf") }
                )
            }
        }
    }
}

@Composable
fun SubjectsCard(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = "Subject name",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "12 Articles",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun RecentArticles(
    onTopicSelected: (topicId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = modifier,
    ) {
        items(10) {
            ArticleCard(
                modifier = Modifier
                    .width(300.dp)
                    .clickable { onTopicSelected(123L) }
            )
        }
    }
}

@Composable
fun ArticleCard(modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = {
                        Text(
                            text = "Environment",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                )
            }
            Text(
                text = "Topic",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to",
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun SubjectsScreenPreview() {
    QuizApplicationTheme {
        SubjectsLearnScreen(
            onSubjectSelected = {},
            onTopicSelected = {}
        )
    }
}