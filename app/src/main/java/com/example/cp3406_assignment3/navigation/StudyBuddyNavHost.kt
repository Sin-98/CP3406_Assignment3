package com.example.studybuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studybuddy.ui.cards.CardsScreen
import com.example.studybuddy.ui.home.HomeScreen
import com.example.studybuddy.ui.quiz.QuizPlayScreen
import com.example.studybuddy.ui.quiz.QuizTopicScreen
import com.example.studybuddy.ui.settings.SettingsScreen
import com.example.studybuddy.ui.statistics.StatisticsScreen

@Composable
fun StudyBuddyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onStartQuiz = { navController.navigate(Screen.Quiz.route) },
                onStudyFlashcards = { navController.navigate(Screen.Cards.route) },
                onOpenTopic = { topic -> navController.navigate(Screen.QuizPlay.createRoute(topic)) }
            )
        }

        composable(Screen.Cards.route) {
            CardsScreen()
        }

        composable(Screen.Quiz.route) {
            QuizTopicScreen(
                onStartQuiz = { topic -> navController.navigate(Screen.QuizPlay.createRoute(topic)) }
            )
        }

        composable(
            route = Screen.QuizPlay.route,
            arguments = listOf(navArgument("topic") { type = NavType.StringType })
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic") ?: "SQL"
            QuizPlayScreen(topic = topic, onExit = { navController.popBackStack(Screen.Quiz.route, false) })
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }
    }
}
