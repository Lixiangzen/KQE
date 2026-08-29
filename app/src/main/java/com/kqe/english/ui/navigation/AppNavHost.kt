package com.kqe.english.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kqe.english.MainViewModel
import com.kqe.english.ui.screens.AboutScreen
import com.kqe.english.ui.screens.DictionaryScreen
import com.kqe.english.ui.screens.EndlessScreen
import com.kqe.english.ui.screens.HomeScreen
import com.kqe.english.ui.screens.MoreScreen
import com.kqe.english.ui.screens.OnboardingScreen
import com.kqe.english.ui.screens.PracticeScreen
import com.kqe.english.ui.screens.SettingsScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PRACTICE = "practice"
    const val DICTIONARY = "dictionary"
    const val ENDLESS = "endless"
    const val MORE = "more"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                viewModel = viewModel,
                onDone = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onPractice = { navController.navigate(Routes.PRACTICE) },
                onMore = { navController.navigate(Routes.MORE) }
            )
        }
        composable(Routes.PRACTICE) {
            PracticeScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.DICTIONARY) {
            DictionaryScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.ENDLESS) {
            EndlessScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.MORE) {
            MoreScreen(
                onBack = { navController.popBackStack() },
                onDictionary = { navController.navigate(Routes.DICTIONARY) },
                onEndless = { navController.navigate(Routes.ENDLESS) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onAbout = { navController.navigate(Routes.ABOUT) }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
