package com.example.studybuddy.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.ui.common.ALL_TOPICS
import com.example.studybuddy.ui.common.topicColor

@Composable
fun CardsScreen(viewModel: CardsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val topics = listOf("All") + ALL_TOPICS

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Flashcards", style = MaterialTheme.typography.titleLarge)
                    Text("Tap a card to flip. Mark as known to track progress.", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.width(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Card")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                topics.forEach { topic ->
                    FilterChip(
                        selected = state.selectedTopic == topic,
                        onClick = { viewModel.selectTopic(topic) },
                        label = { Text(topic) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            val card = state.cards.getOrNull(state.currentIndex)
            if (card == null) {
                Text("No flashcards for this topic yet — add one above.")
            } else {
                Text("${state.currentIndex + 1} / ${state.cards.size}", style = MaterialTheme.typography.bodyMedium)
                LinearProgressIndicator(
                    progress = { (state.currentIndex + 1) / state.cards.size.toFloat() },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                val accent = topicColor(card.topic)
                Card(
                    modifier = Modifier.fillMaxWidth().height(320.dp).clickable { viewModel.flip() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = accent)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                if (state.isAnswerRevealed) "Answer" else "Question",
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text("Tap to flip", color = Color.White.copy(alpha = 0.7f))
                        }
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (state.isAnswerRevealed) card.answer else card.question,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Text("", color = Color.Transparent) // spacer to balance layout
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { viewModel.previous() }, modifier = Modifier.weight(1f)) {
                        Text("Prev")
                    }
                    Button(onClick = { viewModel.markAsKnown() }, modifier = Modifier.weight(1.2f)) {
                        Text("Mark as Known")
                    }
                    OutlinedButton(onClick = { viewModel.next() }, modifier = Modifier.weight(1f)) {
                        Text("Next")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCardDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { q, a, t ->
                viewModel.addCard(q, a, t)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddCardDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf(ALL_TOPICS.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Flashcard") },
        text = {
            Column {
                OutlinedTextField(value = question, onValueChange = { question = it }, label = { Text("Question") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = answer, onValueChange = { answer = it }, label = { Text("Answer") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ALL_TOPICS.forEach { t ->
                        FilterChip(selected = topic == t, onClick = { topic = t }, label = { Text(t) })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (question.isNotBlank() && answer.isNotBlank()) onAdd(question, answer, topic) }) {
                Text("Add")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
