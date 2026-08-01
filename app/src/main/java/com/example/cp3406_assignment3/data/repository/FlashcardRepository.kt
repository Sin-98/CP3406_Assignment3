package com.example.cp3406_assignment3.data.repository

import com.example.cp3406_assignment3.data.local.dao.FlashcardDao
import com.example.cp3406_assignment3.data.remote.DictionaryApiService
import com.example.cp3406_assignment3.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashcardRepository @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val dictionaryApi: DictionaryApiService
) {
    fun getByTopic(topic: String): Flow<List<FlashcardEntity>> = flashcardDao.getByTopic(topic)

    fun getAll(): Flow<List<FlashcardEntity>> = flashcardDao.getAll()

    fun getDue(): Flow<List<FlashcardEntity>> = flashcardDao.getDueForReview()

    suspend fun addManualCard(question: String, answer: String, topic: String) {
        flashcardDao.insert(
            FlashcardEntity(question = question, answer = answer, topic = topic, source = "manual")
        )
    }

    /** Demonstrates advanced API usage: builds a flashcard from a live dictionary lookup. */
    suspend fun addCardFromDictionary(word: String, topic: String = "Vocabulary"): Boolean {
        val entry = dictionaryApi.lookup(word).firstOrNull() ?: return false
        val definition = entry.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition ?: return false
        flashcardDao.insert(
            FlashcardEntity(
                question = "What is \"$word\"?",
                answer = definition,
                topic = topic,
                source = "dictionaryApi"
            )
        )
        return true
    }

    /** Simple spaced-repetition update: correct answers push the next review further out. */
    suspend fun markReviewed(card: FlashcardEntity, wasCorrect: Boolean) {
        val intervalDays = if (wasCorrect) minOf((card.reviewCount + 1) * 2, 30) else 1
        flashcardDao.update(
            card.copy(
                lastReviewed = System.currentTimeMillis(),
                reviewCount = if (wasCorrect) card.reviewCount + 1 else 0,
                nextReviewDue = System.currentTimeMillis() + intervalDays * 86_400_000L
            )
        )
    }

    suspend fun delete(card: FlashcardEntity) = flashcardDao.delete(card)

    suspend fun count(): Int = flashcardDao.count()
}
