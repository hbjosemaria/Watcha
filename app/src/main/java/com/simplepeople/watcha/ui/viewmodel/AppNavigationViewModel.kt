package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.ui.appscreen.common.clases.HomeFilterOptions
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedFavoriteEventFlow
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedHomeFilterFlow
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedScrollToTopFlow
import com.simplepeople.watcha.ui.navigation.AppBarOption
import com.simplepeople.watcha.ui.navigation.AppScreens
import com.simplepeople.watcha.ui.stateholder.AppNavigationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val scrollToTopFlow: SharedScrollToTopFlow.Instance,
    private val homeFilterFlow: SharedHomeFilterFlow.Instance,
    private val favoriteEventFlow: SharedFavoriteEventFlow.Instance
) : ViewModel() {

    val _appNavigationUiState = MutableStateFlow(AppNavigationUiState())
    val appNavigationUiState = _appNavigationUiState.asStateFlow()

    init {
        viewModelScope.launch {
            favoriteEventFlow.favoriteEventFlow.collect {snackbarMessage ->
                _appNavigationUiState.value = _appNavigationUiState.value.copy(
                    showSnackbar = true,
                    textSnackbar = snackbarMessage
                )
            }
        }
    }

    fun resetSnackbar() {
        _appNavigationUiState.value = _appNavigationUiState.value.copy(
            showSnackbar = false
        )
    }

    fun updateBottomBarSelectedIndex(index: Int) {
        _appNavigationUiState.value =
            _appNavigationUiState.value.copy(selectedBottomItemIndex = index)
    }

    fun emitScrollToTopEvent() {
        scrollToTopFlow.emitTopScrollEvent()
    }

    fun emitFilterNowPlayingEvent() {
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.NowPlaying)
    }

    fun emitFilterPopularEvent() {
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Popular)
    }

    fun emitFilterTopRatedEvent() {
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.TopRated)
    }

    fun emitFilterUpcomingEvent() {
        homeFilterFlow.emitFilterEvent(HomeFilterOptions.Upcoming)
    }

    fun navigatingToSearch() {
        _appNavigationUiState.value = _appNavigationUiState.value.copy(
            screenTitle = AppScreens.SearchScreen.name,
            showBottomBar = false,
            appBarOption = AppBarOption.SEARCH
        )
    }

    fun navigatingToHomeScreen() {
        _appNavigationUiState.value = _appNavigationUiState.value.copy(
            showBottomBar = true,
            appBarOption = AppBarOption.HOME,
        )
    }

    fun navigatingToFavoritesScreen() {
        _appNavigationUiState.value = _appNavigationUiState.value.copy(
            showBottomBar = true,
            appBarOption = AppBarOption.FAVORITE,
        )
    }

    fun navigatingToMovieDetails() {
        _appNavigationUiState.value = _appNavigationUiState.value.copy(
            showBottomBar = false,
            appBarOption = AppBarOption.MOVIE_DETAILS,
        )
    }

}