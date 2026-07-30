package com.example.studybuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studybuddy.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcards WHERE topic = :topic ORDER BY nextReviewDue ASC")
    fun getByTopic(topic: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards ORDER BY topic ASC")
    fun getAll(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE nextReviewDue <= :now ORDER BY nextReviewDue ASC")
    fun getDueForReview(now: Long = System.currentTimeMillis()): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: FlashcardEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<FlashcardEntity>)

    @Update
    suspend fun update(card: FlashcardEntity)

    @Delete
    suspend fun delete(card: FlashcardEntity)

    @Query("SELECT COUNT(*) FROM flashcards")
    suspend fun count(): Int
}
