package com.example.studybuddy.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.ui.theme.ErrorRed
import com.example.studybuddy.ui.theme.SuccessGreen

@Composable
fun QuizPlayScreen(
    topic: String,
    onExit: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (state.questions.isEmpty()) viewModel.loadQuiz()
    }

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            when {
                state.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading questions...")
                }

                state.quizError != null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.quizError ?: "", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadQuiz() }) { Text("Retry") }
                    }
                }

                state.finished -> QuizResults(state = state, onDone = onExit)

                state.questions.isNotEmpty() -> QuizQuestionCard(state = state, topic = topic, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun ColumnScope.QuizQuestionCard(state: QuizUiState, topic: String, viewModel: QuizViewModel) {
    val question = state.questions[state.currentIndex]
    val letters = listOf("A", "B", "C", "D")

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Question ${state.currentIndex + 1} / ${state.questions.size}", style = MaterialTheme.typography.bodyMedium)
        Card(shape = RoundedCornerShape(50), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Text(topic, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = { (state.currentIndex) / state.questions.size.toFloat() },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))

    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Text(question.question, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleMedium)
    }
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(question.options) { option ->
            val letter = letters[question.options.indexOf(option)]
            val isSelected = state.selectedAnswer == option
            val isCorrectOption = option == question.correctAnswer
            val showFeedback = state.selectedAnswer != null

            val borderColor = when {
                !showFeedback -> MaterialTheme.colorScheme.outline
                isCorrectOption -> SuccessGreen
                isSelected -> ErrorRed
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            }
            val bgColor = when {
                !showFeedback -> MaterialTheme.colorScheme.surface
                isCorrectOption -> SuccessGreen.copy(alpha = 0.1f)
                isSelected -> ErrorRed.copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                onClick = { viewModel.selectAnswer(option) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                border = BorderStroke(1.5.dp, borderColor)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.size(28.dp)) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text(letter) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(option, modifier = Modifier.weight(1f))
                    if (showFeedback && isCorrectOption) Icon(Icons.Filled.Check, contentDescription = "Correct", tint = SuccessGreen)
                    if (showFeedback && isSelected && !isCorrectOption) Icon(Icons.Filled.Close, contentDescription = "Incorrect", tint = ErrorRed)
                }
            }
        }
    }

    if (state.selectedAnswer != null) {
        Spacer(modifier = Modifier.height(8.dp))
        val correct = state.selectedAnswer == question.correctAnswer
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = if (correct) SuccessGreen.copy(alpha = 0.12f) else ErrorRed.copy(alpha = 0.12f))
        ) {
            Text(
                if (correct) "Correct!" else "Not quite — the correct answer is ${question.correctAnswer}.",
                modifier = Modifier.padding(14.dp),
                color = if (correct) SuccessGreen else ErrorRed,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.nextQuestion() }, modifier = Modifier.fillMaxWidth()) {
            Text(if (state.currentIndex + 1 == state.questions.size) "See Results" else "Next Question")
        }
    }
}

@Composable
private fun ColumnScope.QuizResults(state: QuizUiState, onDone: () -> Unit) {
    val score = state.answered.count { it.wasCorrect }
    val total = state.answered.size
    val percent = if (total > 0) (score * 100 / total) else 0

    Column(modifier = Modifier.fillMaxSize()) {
        Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.15f)), modifier = Modifier.size(72.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = SuccessGreen)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("$percent%", style = MaterialTheme.typography.titleLarge, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text("$score out of $total correct", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultStat("Correct", "$score", SuccessGreen)
                    ResultStat("Wrong", "${total - score}", ErrorRed)
                    ResultStat("Time", "${state.elapsedSeconds}s", MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Review Answers", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(state.answered) { index, answered ->
                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            if (answered.wasCorrect) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = null,
                            tint = if (answered.wasCorrect) SuccessGreen else ErrorRed
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("${index + 1}. ${answered.question.question}", fontWeight = FontWeight.Medium)
                            Text(
                                "Your answer: ${answered.selectedAnswer}",
                                color = if (answered.wasCorrect) SuccessGreen else ErrorRed,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (!answered.wasCorrect) {
                                Text(
                                    "Correct answer: ${answered.question.correctAnswer}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Done") }
    }
}

@Composable
private fun ResultStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}
