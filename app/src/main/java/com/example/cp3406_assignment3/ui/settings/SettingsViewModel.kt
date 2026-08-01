package com.example.cp3406_assignment3.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp3406_assignment3.data.UserPreferences
import com.example.cp3406_assignment3.data.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val settings: StateFlow<UserSettings> = userPreferences.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch { userPreferences.setDarkMode(enabled) }
    fun setDifficulty(value: String) = viewModelScope.launch { userPreferences.setDifficulty(value) }
    fun setQuestionCount(value: Int) = viewModelScope.launch { userPreferences.setQuestionCount(value) }
    fun setDailyGoal(value: Int) = viewModelScope.launch { userPreferences.setDailyGoal(value) }
    fun setHighContrast(enabled: Boolean) = viewModelScope.launch { userPreferences.setHighContrast(enabled) }
}
