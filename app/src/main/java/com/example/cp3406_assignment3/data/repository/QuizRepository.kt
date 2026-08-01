package com.example.cp3406_assignment3.data.repository

import com.example.cp3406_assignment3.data.local.dao.QuizResultDao
import com.example.cp3406_assignment3.data.local.entity.QuizResultEntity
import com.example.cp3406_assignment3.data.remote.TriviaApiService
import com.example.cp3406_assignment3.data.remote.TriviaQuestionDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizResultDao: QuizResultDao,
    private val triviaApi: TriviaApiService
) {
    suspend fun fetchQuestions(amount: Int = 10, category: Int? = 18, difficulty: String? = null): List<TriviaQuestionDto> =
        triviaApi.getQuestions(amount = amount, category = category, difficulty = difficulty).results

    fun getHistory(): Flow<List<QuizResultEntity>> = quizResultDao.getAll()

    suspend fun saveResult(topic: String, score: Int, total: Int) =
        quizResultDao.insert(QuizResultEntity(topic = topic, score = score, totalQuestions = total))

    suspend fun getAverageScore(): Double = quizResultDao.getAverageScorePercent() ?: 0.0

    suspend fun getHighestScore(): Int = quizResultDao.getHighestScorePercent() ?: 0

    suspend fun getTotalQuizCount(): Int = quizResultDao.getTotalQuizCount()
}
