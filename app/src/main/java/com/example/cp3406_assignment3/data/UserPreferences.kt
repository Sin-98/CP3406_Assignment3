package com.example.studybuddy.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "studybuddy_settings")

data class UserSettings(
    val darkMode: Boolean = false,
    val difficulty: String = "medium",
    val questionCount: Int = 10,
    val dailyGoal: Int = 20,
    val highContrast: Boolean = false // accessibility option
)

@Singleton
class UserPreferences @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val DIFFICULTY = stringPreferencesKey("difficulty")
        val QUESTION_COUNT = intPreferencesKey("question_count")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            darkMode = prefs[Keys.DARK_MODE] ?: false,
            difficulty = prefs[Keys.DIFFICULTY] ?: "medium",
            questionCount = prefs[Keys.QUESTION_COUNT] ?: 10,
            dailyGoal = prefs[Keys.DAILY_GOAL] ?: 20,
            highContrast = prefs[Keys.HIGH_CONTRAST] ?: false
        )
    }

    suspend fun setDarkMode(enabled: Boolean) = context.dataStore.edit { it[Keys.DARK_MODE] = enabled }
    suspend fun setDifficulty(value: String) = context.dataStore.edit { it[Keys.DIFFICULTY] = value }
    suspend fun setQuestionCount(value: Int) = context.dataStore.edit { it[Keys.QUESTION_COUNT] = value }
    suspend fun setDailyGoal(value: Int) = context.dataStore.edit { it[Keys.DAILY_GOAL] = value }
    suspend fun setHighContrast(enabled: Boolean) = context.dataStore.edit { it[Keys.HIGH_CONTRAST] = enabled }
}
