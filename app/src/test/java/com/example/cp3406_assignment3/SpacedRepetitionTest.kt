package com.example.cp3406_assignment3

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Mirrors the interval calculation inside FlashcardRepository.markReviewed
 * without needing Room or a real database.
 */
class SpacedRepetitionTest {

    private fun nextIntervalDays(reviewCount: Int, wasCorrect: Boolean): Int =
        if (wasCorrect) minOf((reviewCount + 1) * 2, 30) else 1

    @Test
    fun `correct answer on first review sets 2 day interval`() {
        assertEquals(2, nextIntervalDays(reviewCount = 0, wasCorrect = true))
    }

    @Test
    fun `correct answer increases interval with repeated reviews`() {
        val first = nextIntervalDays(reviewCount = 0, wasCorrect = true)
        val second = nextIntervalDays(reviewCount = 1, wasCorrect = true)
        assertTrue(second > first)
    }

    @Test
    fun `interval is capped at 30 days`() {
        assertEquals(30, nextIntervalDays(reviewCount = 20, wasCorrect = true))
    }

    @Test
    fun `wrong answer resets interval to one day`() {
        assertEquals(1, nextIntervalDays(reviewCount = 10, wasCorrect = false))
    }
}
