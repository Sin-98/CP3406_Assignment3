package com.example.studybuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studybuddy.ui.home.HomeScreen
import com.example.studybuddy.ui.settings.SettingsScreen
import com.example.studybuddy.ui.statistics.StatisticsScreen
import com.example.studybuddy.ui.study.StudyScreen

@Composable
fun StudyBuddyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onStartQuiz = { topic -> navController.navigate(Screen.Study.createRoute("quiz", topic)) },
                onStudyFlashcards = { topic -> navController.navigate(Screen.Study.createRoute("flashcards", topic)) },
                onOpenStatistics = { navController.navigate(Screen.Statistics.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.Study.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("topic") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "flashcards"
            val topic = backStackEntry.arguments?.getString("topic") ?: "Java"
            StudyScreen(initialMode = mode, topic = topic, onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }
    }
}
