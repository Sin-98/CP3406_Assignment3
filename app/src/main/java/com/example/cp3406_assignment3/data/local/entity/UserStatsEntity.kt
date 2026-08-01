package com.example.cp3406_assignment3.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalQuizzesCompleted: Int = 0,
    val totalFlashcardsStudied: Int = 0,
    val lastStudyDate: Long? = null
)
