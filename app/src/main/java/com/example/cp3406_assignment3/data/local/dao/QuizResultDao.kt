package com.example.studybuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.studybuddy.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert
    suspend fun insert(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results ORDER BY date DESC")
    fun getAll(): Flow<List<QuizResultEntity>>

    @Query("SELECT AVG(score * 100.0 / totalQuestions) FROM quiz_results")
    suspend fun getAverageScorePercent(): Double?

    @Query("SELECT MAX(score * 100 / totalQuestions) FROM quiz_results")
    suspend fun getHighestScorePercent(): Int?

    @Query("SELECT COUNT(*) FROM quiz_results")
    suspend fun getTotalQuizCount(): Int
}
