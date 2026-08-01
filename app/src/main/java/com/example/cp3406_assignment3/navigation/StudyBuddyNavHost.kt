package com.example.cp3406_assignment3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.cp3406_assignment3.ui.cards.CardsScreen
import com.example.cp3406_assignment3.ui.home.HomeScreen
import com.example.cp3406_assignment3.ui.quiz.QuizPlayScreen
import com.example.cp3406_assignment3.ui.quiz.QuizTopicScreen
import com.example.cp3406_assignment3.ui.settings.SettingsScreen
import com.example.cp3406_assignment3.ui.statistics.StatisticsScreen

@Composable
fun StudyBuddyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onStartQuiz = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onStudyFlashcards = {
                    navController.navigate(Screen.Cards.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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
