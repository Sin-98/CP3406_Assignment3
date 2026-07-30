package com.example.studybuddy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class BottomNavEntry(val screen: Screen, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomNavItems = listOf(
    BottomNavEntry(Screen.Home, "Home", Icons.Filled.Home),
    BottomNavEntry(Screen.Cards, "Cards", Icons.Filled.Layers),
    BottomNavEntry(Screen.Quiz, "Quiz", Icons.Filled.Help),
    BottomNavEntry(Screen.Statistics, "Stats", Icons.Filled.BarChart),
    BottomNavEntry(Screen.Settings, "Settings", Icons.Filled.Settings)
)

/** Shown on the 5 top-level destinations only; hidden on quiz_play (its own back-navigable flow). */
@Composable
fun StudyBuddyBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { entry ->
            val selected = currentDestination?.hierarchy?.any { it.route == entry.screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(entry.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(entry.icon, contentDescription = entry.label) },
                label = { Text(entry.label) }
            )
        }
    }
}
