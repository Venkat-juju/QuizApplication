package com.example.quizapplication.learn.presentation.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.ui.theme.QuizApplicationTheme

data class SectionsList(
    val sections: List<Section>
)

data class Section(
    val sectionTitle: String,
    val articles: List<Article>
)

data class Article(
    val articleId: Long,
    val articleName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    sectionsList: SectionsList,
    onNavigateToArticle: (articleId: Long) -> Unit,
    onNavigateToBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subjectname",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateToBack
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back icon",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(sectionsList.sections.size) { index ->
                SectionTitle(
                    title = sectionsList.sections[index].sectionTitle,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = Color.Black.copy(alpha = 0.1f))
                sectionsList.sections[index].articles.forEach { article ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToArticle(article.articleId) }
                            .padding(12.dp)
                            .padding(vertical = 4.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_test_filled),
                            contentDescription = "article icon",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = article.articleName,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Divider(color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview
@Composable
fun ArticleListScreenPreview() {
    QuizApplicationTheme {
        ArticleListScreen(
            sectionsList = SectionsList(
                sections = listOf(
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                    Section("Section Title", articles = listOf(Article(1, "article 1"), Article(1, "article 2"), Article(1, "article 3"), Article(1, "article 4"), Article(1, "article 5"))),
                )
            ),
            onNavigateToArticle = {},
            onNavigateToBack = {}
        )
    }
}