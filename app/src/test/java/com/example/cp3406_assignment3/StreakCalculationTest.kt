package com.example.cp3406_assignment3

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

/** Mirrors StatsRepository.recordStudySession streak logic. */
class StreakCalculationTest {

    private fun computeNewStreak(lastStudyDate: Long?, currentStreak: Int, now: Long): Int {
        val oneDayMs = TimeUnit.DAYS.toMillis(1)
        return when {
            lastStudyDate == null -> 1
            now - lastStudyDate < oneDayMs -> currentStreak
            now - lastStudyDate < oneDayMs * 2 -> currentStreak + 1
            else -> 1
        }
    }

    @Test
    fun `first ever session starts streak at 1`() {
        assertEquals(1, computeNewStreak(lastStudyDate = null, currentStreak = 0, now = 0L))
    }

    @Test
    fun `studying again same day does not increase streak`() {
        val now = TimeUnit.DAYS.toMillis(5)
        val lastStudy = now - TimeUnit.HOURS.toMillis(2)
        assertEquals(3, computeNewStreak(lastStudyDate = lastStudy, currentStreak = 3, now = now))
    }

    @Test
    fun `studying next consecutive day increases streak`() {
        val now = TimeUnit.DAYS.toMillis(5)
        val lastStudy = now - TimeUnit.HOURS.toMillis(30)
        assertEquals(4, computeNewStreak(lastStudyDate = lastStudy, currentStreak = 3, now = now))
    }

    @Test
    fun `missing a day resets streak to 1`() {
        val now = TimeUnit.DAYS.toMillis(10)
        val lastStudy = now - TimeUnit.DAYS.toMillis(3)
        assertEquals(1, computeNewStreak(lastStudyDate = lastStudy, currentStreak = 7, now = now))
    }
}
