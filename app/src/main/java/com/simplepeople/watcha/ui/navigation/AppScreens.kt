package com.simplepeople.watcha.ui.navigation

import com.simplepeople.watcha.R

abstract class AppScreens(
    open val route: String,
    open val name: Int,
)

sealed class MainAppScreens(
    override val route: String,
    override val name: Int,
) : AppScreens(route = route, name = name) {
    data object MainScreen : MainAppScreens("main_main", R.string.main)
    data object HomeScreen : MainAppScreens("main_home", R.string.home)
    data object MovieDetailsScreen : MainAppScreens("main_movie_details", R.string.movie_details) {
        fun buildArgRoute(value: Long): String {
            return "$route/${value}"
        }

        fun buildRoute(): String {
            return "$route/{${NavigationVariableNames.MOVIE_ID.variableName}}"
        }
    }
    data object YoutubeScreen : MainAppScreens("main_youtube", R.string.youtube)  {
        fun buildArgRoute(value: String): String {
            return "$route/${value}"
        }

        fun buildRoute(): String {
            return "$route/{${NavigationVariableNames.VIDEO_KEY.variableName}}"
        }
    }
    data object FavoriteScreen : MainAppScreens("main_favorites", R.string.list_favorites)
    data object SearchScreen : MainAppScreens("main_search", R.string.search)
    data object SettingsScreen : MainAppScreens("main_settings", R.string.settings)
    data object ProfileScreen : MainAppScreens("main_profile", R.string.user_profile)
}

sealed class SettingsAppScreens(
    override val route: String,
    override val name: Int,
) : AppScreens(route = route, name = name) {
    data object SettingsScreen : SettingsAppScreens("settings_settings", R.string.settings)
    data object SettingsLanguageScreen :
        SettingsAppScreens("settings_language", R.string.settings_language)
}

sealed class AuthAppScreens(
    override val route: String,
    override val name: Int,
) : AppScreens(route = route, name = name) {
    data object SignInScreen : AuthAppScreens("sign_in", R.string.sign_in_screen)
    data object SignUpScreen : AuthAppScreens("sign_up", R.string.sign_up_screen)
    data object AuthScreen : AuthAppScreens("auth", R.string.auth_screen)
}