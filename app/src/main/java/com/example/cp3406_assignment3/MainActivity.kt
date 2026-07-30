package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.navigation.StudyBuddyBottomBar
import com.example.studybuddy.navigation.StudyBuddyNavHost
import com.example.studybuddy.ui.settings.SettingsViewModel
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Read dark mode from the Settings screen's persisted DataStore value so the
            // toggle actually affects the whole app, not just system default.
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settings by settingsViewModel.settings.collectAsState()

            StudyBuddyTheme(darkTheme = settings.darkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { StudyBuddyBottomBar(navController) }
                    ) { padding ->
                        Surface(modifier = Modifier.padding(padding)) {
                            StudyBuddyNavHost(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
