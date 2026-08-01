package com.example.cp3406_assignment3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cp3406_assignment3.data.local.dao.FlashcardDao
import com.example.cp3406_assignment3.data.local.dao.QuizResultDao
import com.example.cp3406_assignment3.data.local.dao.UserStatsDao
import com.example.cp3406_assignment3.data.local.entity.FlashcardEntity
import com.example.cp3406_assignment3.data.local.entity.QuizResultEntity
import com.example.cp3406_assignment3.data.local.entity.UserStatsEntity

@Database(
    entities = [FlashcardEntity::class, QuizResultEntity::class, UserStatsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StudyBuddyDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun userStatsDao(): UserStatsDao
}
