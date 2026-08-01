package com.example.cp3406_assignment3.data.repository

import com.example.cp3406_assignment3.data.local.dao.UserStatsDao
import com.example.cp3406_assignment3.data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val userStatsDao: UserStatsDao
) {
    fun observeStats(): Flow<UserStatsEntity?> = userStatsDao.observeStats()

    /** Called once per completed study session (quiz or flashcard set). Updates the daily streak. */
    suspend fun recordStudySession(flashcardsStudiedDelta: Int = 0, quizCompleted: Boolean = false) {
        val current = userStatsDao.getStatsOnce() ?: UserStatsEntity()
        val now = System.currentTimeMillis()
        val oneDayMs = TimeUnit.DAYS.toMillis(1)

        val newStreak = when {
            current.lastStudyDate == null -> 1
            now - current.lastStudyDate < oneDayMs -> current.currentStreak // already studied today
            now - current.lastStudyDate < oneDayMs * 2 -> current.currentStreak + 1 // consecutive day
            else -> 1 // streak broken
        }

        userStatsDao.upsert(
            current.copy(
                currentStreak = newStreak,
                longestStreak = maxOf(newStreak, current.longestStreak),
                totalQuizzesCompleted = current.totalQuizzesCompleted + if (quizCompleted) 1 else 0,
                totalFlashcardsStudied = current.totalFlashcardsStudied + flashcardsStudiedDelta,
                lastStudyDate = now
            )
        )
    }
}
