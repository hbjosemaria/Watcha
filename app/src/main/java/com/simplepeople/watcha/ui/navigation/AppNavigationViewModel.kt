package com.simplepeople.watcha.ui.navigation

//TODO: delete this file as AppNavigation now doesn't have UI to manage and it works as a navigation router
//@HiltViewModel
//class AppNavigationViewModel @Inject constructor(
//    private val scrollToTopFlow: SharedScrollToTopFlow.Instance,
//    private val homeFilterFlow: SharedHomeFilterFlow.Instance,
//    private val favoriteEventFlow: SharedFavoriteEventFlow.Instance
//) : ViewModel() {
//
//    val appNavigationUiState = mutableStateOf(AppNavigationUiState())
//    val snackBarItem = mutableStateOf(SnackbarItem())
//
//    init {
//        viewModelScope.launch {
//            favoriteEventFlow.favoriteEventFlow.collect {snackbarMessage ->
//                snackBarItem.value = snackBarItem.value.copy(
//                    showSnackbar = true,
//                    textSnackbar = snackbarMessage
//                )
//            }
//        }
//    }
//
//    fun resetSnackbar() {
//        snackBarItem.value = snackBarItem.value.copy(
//            showSnackbar = false
//        )
//    }
//
//    fun updateNavigationBarSelectedIndex(index: Int) {
//        appNavigationUiState.value =
//            appNavigationUiState.value.copy(selectedNavigationItemIndex = index)
//    }
//
//    fun emitScrollToTopEvent() {
//        scrollToTopFlow.emitTopScrollEvent()
//    }
//
//
//    fun emitFilterNowPlayingEvent() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            selectedTopBarItem = TopBarItemOption.NOW_PLAYING
//        )
//        homeFilterFlow.emitFilterEvent(HomeFilterOptions.NowPlaying)
//    }
//
//    fun emitFilterPopularEvent() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            selectedTopBarItem = TopBarItemOption.POPULAR
//        )
//        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Popular)
//    }
//
//    fun emitFilterTopRatedEvent() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            selectedTopBarItem = TopBarItemOption.TOP_RATED
//        )
//        homeFilterFlow.emitFilterEvent(HomeFilterOptions.TopRated)
//    }
//
//    fun emitFilterUpcomingEvent() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            selectedTopBarItem = TopBarItemOption.UPCOMING
//        )
//        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Upcoming)
//    }
//
//    fun navigatingToSearch() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            screenTitle = AppScreens.SearchScreen.name,
//            showNavigationBar = false,
//            appBarOption = AppBarOption.SEARCH
//        )
//    }
//
//    fun navigatingToHomeScreen() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            showNavigationBar = true,
//            appBarOption = AppBarOption.HOME,
//        )
//    }
//
//    fun navigatingToFavoritesScreen() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            showNavigationBar = true,
//            appBarOption = AppBarOption.FAVORITE,
//        )
//    }
//
//    fun navigatingToMovieDetails() {
//        appNavigationUiState.value = appNavigationUiState.value.copy(
//            showNavigationBar = false,
//            appBarOption = AppBarOption.MOVIE_DETAILS,
//        )
//    }
//
//}