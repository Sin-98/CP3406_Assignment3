package com.example.studybuddy.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.local.entity.FlashcardEntity
import com.example.studybuddy.data.repository.FlashcardRepository
import com.example.studybuddy.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardsUiState(
    val selectedTopic: String = "All",
    val cards: List<FlashcardEntity> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerRevealed: Boolean = false
)

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        loadCards("All")
    }

    fun selectTopic(topic: String) {
        _uiState.value = _uiState.value.copy(selectedTopic = topic, currentIndex = 0, isAnswerRevealed = false)
        loadCards(topic)
    }

    private fun loadCards(topic: String) {
        viewModelScope.launch {
            val flow = if (topic == "All") flashcardRepository.getAll() else flashcardRepository.getByTopic(topic)
            flow.collect { cards ->
                _uiState.value = _uiState.value.copy(cards = cards)
            }
        }
    }

    fun flip() {
        _uiState.value = _uiState.value.copy(isAnswerRevealed = !_uiState.value.isAnswerRevealed)
    }

    fun next() {
        val state = _uiState.value
        if (state.cards.isEmpty()) return
        val nextIndex = (state.currentIndex + 1).coerceAtMost(state.cards.size - 1)
        _uiState.value = state.copy(currentIndex = nextIndex, isAnswerRevealed = false)
    }

    fun previous() {
        val state = _uiState.value
        val prevIndex = (state.currentIndex - 1).coerceAtLeast(0)
        _uiState.value = state.copy(currentIndex = prevIndex, isAnswerRevealed = false)
    }

    fun markAsKnown() {
        val state = _uiState.value
        val card = state.cards.getOrNull(state.currentIndex) ?: return
        viewModelScope.launch {
            flashcardRepository.markReviewed(card, wasCorrect = true)
            statsRepository.recordStudySession(flashcardsStudiedDelta = 1)
        }
        next()
    }

    fun addCard(question: String, answer: String, topic: String) {
        viewModelScope.launch {
            flashcardRepository.addManualCard(question, answer, topic)
        }
    }
}
