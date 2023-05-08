package com.example.quizapplication.quiz.presentation.compoenents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@Composable
fun InitialIcon(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.substring(0..1).uppercase(),
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
fun InitialIconPreview() {
    QuizApplicationTheme {
        InitialIcon(name = "Tamil")
    }
}
