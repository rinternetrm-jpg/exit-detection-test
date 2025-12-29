package com.exitreminder.exitdetection.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exitreminder.exitdetection.presentation.screens.home.HomeScreen
import com.exitreminder.exitdetection.presentation.screens.analysis.AnalysisScreen
import com.exitreminder.exitdetection.presentation.screens.reminder.ReminderConfigScreen
import com.exitreminder.exitdetection.presentation.screens.livetest.LiveTestScreen
import com.exitreminder.exitdetection.presentation.screens.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val ANALYSIS = "analysis"
    const val REMINDER_CONFIG = "reminder_config/{profileJson}"
    const val LIVE_TEST = "live_test/{reminderId}"
    const val SETTINGS = "settings"

    fun reminderConfig(profileJson: String) = "reminder_config/$profileJson"
    fun liveTest(reminderId: Long) = "live_test/$reminderId"
}

@Composable
fun ExitDetectionNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToNewReminder = {
                    navController.navigate(Routes.ANALYSIS)
                },
                onNavigateToLiveTest = { reminderId ->
                    navController.navigate(Routes.liveTest(reminderId))
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.ANALYSIS) {
            AnalysisScreen(
                onNavigateBack = { navController.popBackStack() },
                onAnalysisComplete = { profileId ->
                    navController.navigate(Routes.reminderConfig(profileId.toString())) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(
            route = Routes.REMINDER_CONFIG,
            arguments = listOf(
                navArgument("profileJson") { type = NavType.StringType }
            )
        ) {
            ReminderConfigScreen(
                onNavigateBack = { navController.popBackStack() },
                onReminderCreated = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.LIVE_TEST,
            arguments = listOf(
                navArgument("reminderId") { type = NavType.LongType }
            )
        ) {
            LiveTestScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
