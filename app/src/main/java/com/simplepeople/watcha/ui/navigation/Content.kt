package com.simplepeople.watcha.ui.navigation

// TODO: Delete this file as it is now on AppNavigation.kt
//@Stable
//@Composable
//fun Content(
//    innerPadding: PaddingValues,
//    navController: NavHostController,
//    appNavigationViewModel : AppNavigationViewModel = hiltViewModel()
//) {
//    NavHost(
//        navController = navController,
//        startDestination = AppScreens.HomeScreen.route,
//        modifier = Modifier
//                .padding(innerPadding)
//    ) {
//        composable(
//            AppScreens.HomeScreen.route,
//            enterTransition = {
//                EnterTransition.None
//            }
//        ) {
//            appNavigationViewModel.navigatingToHomeScreen()
//            HomeScreen { movieId: Long ->
//                navController.navigate(
//                    AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
//                )
//            }
//        }
//        composable(
//            AppScreens.MovieDetailsScreen.buildRoute(),
//            arguments = listOf(navArgument("movieId") {
//                type = NavType.LongType
//            })
//        ) {
//            appNavigationViewModel.navigatingToMovieDetails()
//            val movieId = it.arguments?.getLong("movieId") ?: 1
//            MovieDetailsScreen(
//                movieId = movieId
//            )
//        }
//        composable(
//            AppScreens.FavoriteScreen.route,
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None }
//        ) {
//            appNavigationViewModel.navigatingToFavoritesScreen()
//            FavoriteScreen { movieId: Long ->
//                navController.navigate(
//                    AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
//                )
//            }
//        }
//        composable(
//            AppScreens.SearchScreen.route,
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None }
//        ) {
//            appNavigationViewModel.navigatingToSearch()
//            SearchScreen(
//                navigateToMovieDetails = {movieId : Long ->
//                    navController.navigate(
//                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
//                    )
//                }
//            )
//        }
//    }
//}
//
//
//sealed class AppScreens(
//    val route: String,
//    val name: Int
//) {
//    data object HomeScreen : AppScreens("home", R.string.home)
//    data object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details) {
//        fun buildArgRoute(value: Long) : String {
//            return "$route/${value}"
//        }
//        fun buildRoute() : String {
//            return "$route/{movieId}"
//        }
//    }
//    data object FavoriteScreen : AppScreens("favorites", R.string.list_favorites)
//    data object SearchScreen : AppScreens("search", R.string.search)
//}