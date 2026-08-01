package com.example.cp3406_assignment3.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp3406_assignment3.data.UserPreferences
import com.example.cp3406_assignment3.data.repository.QuizRepository
import com.example.cp3406_assignment3.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val streak: Int = 0,
    val flashcardsStudied: Int = 0,
    val quizzesCompleted: Int = 0,
    val averageScore: Double = 0.0,
    val dailyGoal: Int = 20,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val statsRepository: StatsRepository,
    private val quizRepository: QuizRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            statsRepository.observeStats().collect { stats ->
                _uiState.value = _uiState.value.copy(
                    streak = stats?.currentStreak ?: 0,
                    flashcardsStudied = stats?.totalFlashcardsStudied ?: 0,
                    quizzesCompleted = stats?.totalQuizzesCompleted ?: 0,
                    isLoading = false
                )
            }
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(averageScore = quizRepository.getAverageScore())
        }
        viewModelScope.launch {
            userPreferences.settings.collect { s ->
                _uiState.value = _uiState.value.copy(dailyGoal = s.dailyGoal)
            }
        }
    }
}
