package com.example.studybuddy.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.entity.QuizResultEntity
import com.example.studybuddy.data.repository.QuizRepository
import com.example.studybuddy.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatisticsUiState(
    val totalFlashcardsStudied: Int = 0,
    val totalQuizzesCompleted: Int = 0,
    val highestScore: Int = 0,
    val currentStreak: Int = 0,
    val history: List<QuizResultEntity> = emptyList(),
    val averageByTopic: Map<String, Double> = emptyMap()
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statsRepository: StatsRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            statsRepository.observeStats().collect { stats ->
                _uiState.value = _uiState.value.copy(
                    totalFlashcardsStudied = stats?.totalFlashcardsStudied ?: 0,
                    totalQuizzesCompleted = stats?.totalQuizzesCompleted ?: 0,
                    currentStreak = stats?.currentStreak ?: 0
                )
            }
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(highestScore = quizRepository.getHighestScore())
        }
        viewModelScope.launch {
            quizRepository.getHistory().collect { results ->
                val byTopic = results
                    .groupBy { it.topic }
                    .mapValues { (_, list) -> list.map { it.score * 100.0 / it.totalQuestions }.average() }
                _uiState.value = _uiState.value.copy(history = results, averageByTopic = byTopic)
            }
        }
    }
}
