package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.ui.appscreen.common.clases.HomeFilterOptions
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedFavoriteEventFlow
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedHomeFilterFlow
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedScrollToTopFlow
import com.simplepeople.watcha.ui.navigation.AppBarOption
import com.simplepeople.watcha.ui.navigation.AppScreens
import com.simplepeople.watcha.ui.stateholder.AppNavigationUiState
import com.simplepeople.watcha.ui.stateholder.SnackBarItem
import com.simplepeople.watcha.ui.stateholder.TopBarItemOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val scrollToTopFlow: SharedScrollToTopFlow.Instance,
    private val homeFilterFlow: SharedHomeFilterFlow.Instance,
    private val favoriteEventFlow: SharedFavoriteEventFlow.Instance
) : ViewModel() {

    val appNavigationUiState = mutableStateOf(AppNavigationUiState())
    val snackBarItem = mutableStateOf(SnackBarItem())

    init {
        viewModelScope.launch {
            favoriteEventFlow.favoriteEventFlow.collect {snackbarMessage ->
                snackBarItem.value = snackBarItem.value.copy(
                    showSnackbar = true,
                    textSnackbar = snackbarMessage
                )
            }
        }
    }

    fun resetSnackbar() {
        snackBarItem.value = snackBarItem.value.copy(
            showSnackbar = false
        )
    }

    fun updateNavigationBarSelectedIndex(index: Int) {
        appNavigationUiState.value =
            appNavigationUiState.value.copy(selectedNavigationItemIndex = index)
    }

    fun emitScrollToTopEvent() {
        scrollToTopFlow.emitTopScrollEvent()
    }


    fun emitFilterNowPlayingEvent() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            selectedTopBarItem = TopBarItemOption.NOW_PLAYING
        )
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.NowPlaying)
    }

    fun emitFilterPopularEvent() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            selectedTopBarItem = TopBarItemOption.POPULAR
        )
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Popular)
    }

    fun emitFilterTopRatedEvent() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            selectedTopBarItem = TopBarItemOption.TOP_RATED
        )
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.TopRated)
    }

    fun emitFilterUpcomingEvent() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            selectedTopBarItem = TopBarItemOption.UPCOMING
        )
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Upcoming)
    }

    fun navigatingToSearch() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            screenTitle = AppScreens.SearchScreen.name,
            showNavigationBar = false,
            appBarOption = AppBarOption.SEARCH
        )
    }

    fun navigatingToHomeScreen() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            showNavigationBar = true,
            appBarOption = AppBarOption.HOME,
        )
    }

    fun navigatingToFavoritesScreen() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            showNavigationBar = true,
            appBarOption = AppBarOption.FAVORITE,
        )
    }

    fun navigatingToMovieDetails() {
        appNavigationUiState.value = appNavigationUiState.value.copy(
            showNavigationBar = false,
            appBarOption = AppBarOption.MOVIE_DETAILS,
        )
    }

}