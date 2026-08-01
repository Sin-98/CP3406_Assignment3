package com.example.studybuddy.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.ui.theme.BrandPurple

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsState()

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Settings", style = MaterialTheme.typography.titleLarge) }
            item { Text("Customize your study experience.", style = MaterialTheme.typography.bodyMedium) }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = BrandPurple), modifier = Modifier.size(44.dp)) {}
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Local Profile", fontWeight = FontWeight.Medium)
                            Text("All data stored on this device only", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                SettingsCard(title = "Dark Mode", subtitle = "Easier on the eyes at night") {
                    Switch(checked = settings.darkMode, onCheckedChange = viewModel::setDarkMode)
                }
            }

            item {
                SettingsCard(title = "High Contrast", subtitle = "Accessibility: stronger color contrast") {
                    Switch(checked = settings.highContrast, onCheckedChange = viewModel::setHighContrast)
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Quiz Difficulty", fontWeight = FontWeight.Medium)
                        Text("Sets question complexity", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("easy", "medium", "hard").forEach { level ->
                                DifficultyChip(
                                    label = level.replaceFirstChar { it.uppercase() },
                                    selected = settings.difficulty == level,
                                    onClick = { viewModel.setDifficulty(level) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Number of Questions", fontWeight = FontWeight.Medium)
                                Text("Per quiz session", style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("${settings.questionCount}", style = MaterialTheme.typography.titleLarge, color = BrandPurple)
                        }
                        Slider(
                            value = settings.questionCount.toFloat(),
                            onValueChange = { viewModel.setQuestionCount(it.toInt()) },
                            valueRange = 5f..20f,
                            steps = 14
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("5", style = MaterialTheme.typography.bodyMedium)
                            Text("20", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Daily Goal", fontWeight = FontWeight.Medium)
                                Text("Flashcards to study per day", style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("${settings.dailyGoal}", style = MaterialTheme.typography.titleLarge, color = BrandPurple)
                        }
                        Slider(
                            value = settings.dailyGoal.toFloat(),
                            onValueChange = { viewModel.setDailyGoal(it.toInt()) },
                            valueRange = 5f..50f,
                            steps = 8
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("5", style = MaterialTheme.typography.bodyMedium)
                            Text("50", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(title: String, subtitle: String, trailing: @Composable () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            trailing()
        }
    }
}

@Composable
private fun DifficultyChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) BrandPurple.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (selected) androidx.compose.foundation.BorderStroke(1.5.dp, BrandPurple) else null,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = if (selected) BrandPurple else Color.Unspecified, fontWeight = FontWeight.Medium)
        }
    }
}
