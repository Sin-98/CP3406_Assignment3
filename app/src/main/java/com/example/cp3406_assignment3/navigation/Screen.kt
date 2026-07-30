package com.example.studybuddy.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Study : Screen("study/{mode}/{topic}") {
        fun createRoute(mode: String, topic: String) = "study/$mode/$topic"
    }

    data object Settings : Screen("settings")
    data object Statistics : Screen("statistics")
}
