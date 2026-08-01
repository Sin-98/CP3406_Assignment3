package com.example.cp3406_assignment3.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis()
)
