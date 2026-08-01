package com.example.cp3406_assignment3

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pure-logic test of quiz scoring, mirroring QuizViewModel.nextQuestion.
 * No Android framework dependency -> runs as a fast local JVM test.
 */
class QuizScoringTest {

    private fun scoreQuiz(answers: List<String>, correctAnswers: List<String>): Int =
        answers.zip(correctAnswers).count { (given, correct) -> given == correct }

    @Test
    fun `all correct answers gives full score`() {
        val given = listOf("SELECT", "Java", "TCP")
        val correct = listOf("SELECT", "Java", "TCP")
        assertEquals(3, scoreQuiz(given, correct))
    }

    @Test
    fun `no correct answers gives zero score`() {
        val given = listOf("INSERT", "Python", "UDP")
        val correct = listOf("SELECT", "Java", "TCP")
        assertEquals(0, scoreQuiz(given, correct))
    }

    @Test
    fun `partial correct answers gives partial score`() {
        val given = listOf("SELECT", "Python", "TCP")
        val correct = listOf("SELECT", "Java", "TCP")
        assertEquals(2, scoreQuiz(given, correct))
    }
}
