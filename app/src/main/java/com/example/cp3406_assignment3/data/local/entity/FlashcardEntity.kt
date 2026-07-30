package com.example.studybuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val answer: String,
    val topic: String,
    val source: String = "manual", // "manual" | "dictionaryApi"
    val lastReviewed: Long? = null,
    val reviewCount: Int = 0,
    val nextReviewDue: Long = System.currentTimeMillis()
)
