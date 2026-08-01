package com.example.cp3406_assignment3.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp3406_assignment3.data.UserPreferences
import com.example.cp3406_assignment3.data.remote.TriviaQuestionDto
import com.example.cp3406_assignment3.data.repository.QuizRepository
import com.example.cp3406_assignment3.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

data class AnsweredQuestion(
    val question: QuizQuestion,
    val selectedAnswer: String
) {
    val wasCorrect: Boolean get() = selectedAnswer == question.correctAnswer
}

data class QuizUiState(
    val topic: String = "SQL",
    val isLoading: Boolean = false,
    val quizError: String? = null,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val answered: List<AnsweredQuestion> = emptyList(),
    val startTimeMs: Long = 0L,
    val finished: Boolean = false,
    val elapsedSeconds: Int = 0
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val statsRepository: StatsRepository,
    private val userPreferences: UserPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState(topic = savedStateHandle.get<String>("topic") ?: "SQL"))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun loadQuiz() {
        _uiState.value = _uiState.value.copy(isLoading = true, quizError = null)
        viewModelScope.launch {
            try {
                val settings = userPreferences.settings.first()
                val questions = quizRepository.fetchQuestions(
                    amount = settings.questionCount,
                    difficulty = settings.difficulty
                ).map { it.toQuizQuestion() }
                _uiState.value = _uiState.value.copy(
                    questions = questions,
                    currentIndex = 0,
                    selectedAnswer = null,
                    answered = emptyList(),
                    finished = false,
                    isLoading = false,
                    startTimeMs = System.currentTimeMillis(),
                    quizError = if (questions.isEmpty()) "No questions returned. Try again." else null
                )
            } catch (e: Exception) {
                android.util.Log.e("QuizViewModel", "Failed to load quiz questions", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    quizError = "Couldn't load quiz questions. Check your connection and try again."
                )
            }
        }
    }

    fun selectAnswer(answer: String) {
        if (_uiState.value.selectedAnswer != null) return // already answered this question
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun nextQuestion() {
        val state = _uiState.value
        val current = state.questions.getOrNull(state.currentIndex) ?: return
        val selected = state.selectedAnswer ?: return
        val newAnswered = state.answered + AnsweredQuestion(current, selected)
        val nextIndex = state.currentIndex + 1
        val finished = nextIndex >= state.questions.size

        if (finished) {
            val score = newAnswered.count { it.wasCorrect }
            viewModelScope.launch {
                quizRepository.saveResult(state.topic, score, state.questions.size)
                statsRepository.recordStudySession(quizCompleted = true)
            }
        }

        _uiState.value = state.copy(
            answered = newAnswered,
            currentIndex = nextIndex,
            selectedAnswer = null,
            finished = finished,
            elapsedSeconds = ((System.currentTimeMillis() - state.startTimeMs) / 1000).toInt()
        )
    }
}

fun TriviaQuestionDto.toQuizQuestion(): QuizQuestion {
    val options = (incorrect_answers + correct_answer).map { unescapeHtml(it) }.shuffled()
    return QuizQuestion(
        question = unescapeHtml(question),
        options = options,
        correctAnswer = unescapeHtml(correct_answer)
    )
}

private fun unescapeHtml(text: String): String = text
    .replace("&quot;", "\"")
    .replace("&#039;", "'")
    .replace("&amp;", "&")
    .replace("&eacute;", "\u00e9")
