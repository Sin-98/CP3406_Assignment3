package com.example.studybuddy.ui.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.ui.common.topicColor
import com.example.studybuddy.ui.theme.BrandPurple

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Statistics", style = MaterialTheme.typography.titleLarge) }
            item { Text("Track your learning progress over time.", style = MaterialTheme.typography.bodyMedium) }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    StatTile("${state.currentStreak}", "Study Streak", Modifier.weight(1f))
                    StatTile("${state.totalFlashcardsStudied}", "Flashcards Studied", Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    StatTile("${state.totalQuizzesCompleted}", "Quizzes Completed", Modifier.weight(1f))
                    StatTile("${state.highestScore}%", "Highest Score", Modifier.weight(1f))
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Score History", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (state.history.isEmpty()) {
                            Text("Complete a quiz to see your score history.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            val scores = state.history.reversed().map { it.score * 100 / it.totalQuestions }
                            LineChart(scores = scores, modifier = Modifier.fillMaxWidth().height(160.dp))
                        }
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Average Score by Topic", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (state.averageByTopic.isEmpty()) {
                            Text("No quiz results yet.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            BarChart(data = state.averageByTopic, modifier = Modifier.fillMaxWidth().height(180.dp))
                        }
                    }
                }
            }

            item { Text("Recent Quizzes", style = MaterialTheme.typography.titleMedium) }
            items(state.history.take(10)) { result ->
                val percent = result.score * 100 / result.totalQuestions
                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(result.topic, fontWeight = FontWeight.Medium)
                            Text("${result.score}/${result.totalQuestions} correct", style = MaterialTheme.typography.bodyMedium)
                        }
                        Card(shape = RoundedCornerShape(50), colors = CardDefaults.cardColors(containerColor = topicColor(result.topic))) {
                            Text("$percent%", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = androidx.compose.ui.graphics.Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    Card(shape = RoundedCornerShape(16.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** Simple hand-rolled line chart (no external charting library needed). */
@Composable
private fun LineChart(scores: List<Int>, modifier: Modifier = Modifier) {
    val purple = BrandPurple
    Canvas(modifier = modifier) {
        if (scores.size < 2) {
            // single point: just draw a dot in the middle
            val cx = size.width / 2
            val cy = size.height * (1 - (scores.firstOrNull() ?: 0) / 100f)
            drawCircle(color = purple, radius = 8f, center = Offset(cx, cy))
            return@Canvas
        }
        val stepX = size.width / (scores.size - 1)
        val points = scores.mapIndexed { index, score ->
            Offset(index * stepX, size.height * (1 - score / 100f))
        }
        for (i in 0 until points.size - 1) {
            drawLine(color = purple, start = points[i], end = points[i + 1], strokeWidth = 4f)
        }
        points.forEach { point -> drawCircle(color = purple, radius = 6f, center = point) }
    }
}

/** Simple hand-rolled bar chart for average score per topic. */
@Composable
private fun BarChart(data: Map<String, Double>, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        data.forEach { (topic, avg) ->
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(1f - (avg / 100f).toFloat().coerceIn(0f, 1f)))
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .weight((avg / 100f).toFloat().coerceIn(0.02f, 1f))
                        .fillMaxWidth(0.6f)
                        .background(topicColor(topic), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(topic.take(4), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
