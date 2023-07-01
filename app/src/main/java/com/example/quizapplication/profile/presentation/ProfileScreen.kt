package com.example.quizapplication.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapplication.R
import com.example.quizapplication.ui.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Profile")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ProfileView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp)
            )
            SavedView()
        }
    }
}

@Composable
fun ProfileView(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_profile_outlined),
            contentDescription = "Profile image",
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(50))
                .border(BorderStroke(4.dp, color = Color.Black), shape = RoundedCornerShape(50))
                .padding(12.dp)
        )
        Text(
            text = "Venkataramanan P",
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun SavedView() {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Saved",
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Bookmarks icon",
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(text = "Bookmarks")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Bookmarks icon",
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(text = "History")
        }
    }
}

@Preview
@Composable
fun ProfileViewPreview() {
    QuizApplicationTheme {
        ProfileView()
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    QuizApplicationTheme {
        ProfileScreen()
    }
}