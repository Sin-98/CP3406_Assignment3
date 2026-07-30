package com.example.studybuddy.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Cards : Screen("cards")
    data object Quiz : Screen("quiz")

    data object QuizPlay : Screen("quiz_play/{topic}") {
        fun createRoute(topic: String) = "quiz_play/$topic"
    }

    data object Settings : Screen("settings")
    data object Statistics : Screen("statistics")
}
