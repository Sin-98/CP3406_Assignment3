package com.example.cp3406_assignment3.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cp3406_assignment3.ui.common.ALL_TOPICS
import com.example.cp3406_assignment3.ui.common.topicColor
import com.example.cp3406_assignment3.ui.common.topicIcon
import com.example.cp3406_assignment3.ui.settings.SettingsViewModel

@Composable
fun QuizTopicScreen(
    onStartQuiz: (String) -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    var selectedTopic by remember { mutableStateOf(ALL_TOPICS.first()) }
    val settings by settingsViewModel.settings.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.height(72.dp).aspectRatio(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Quiz Challenge", style = MaterialTheme.typography.titleLarge)
            Text("Questions pulled live from Open Trivia DB.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(20.dp))

            Text("Choose a topic", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().height(240.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ALL_TOPICS) { topic ->
                    val selected = selectedTopic == topic
                    Card(
                        onClick = { selectedTopic = topic },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) topicColor(topic).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, topicColor(topic)) else null
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = topicColor(topic)), modifier = Modifier.height(44.dp).aspectRatio(1f)) {
                                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                    Icon(topicIcon(topic), contentDescription = null, tint = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(topic, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    QuizSetting("Difficulty", settings.difficulty.replaceFirstChar { it.uppercase() })
                    QuizSetting("Questions", "${settings.questionCount}")
                    QuizSetting("Daily Goal", "${settings.dailyGoal} cards")
                }
            }
            Text("Adjust these in Settings.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { onStartQuiz(selectedTopic) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.Filled.Bolt, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Start ${settings.questionCount}-Question Quiz")
            }
        }
    }
}

@Composable
private fun QuizSetting(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}
