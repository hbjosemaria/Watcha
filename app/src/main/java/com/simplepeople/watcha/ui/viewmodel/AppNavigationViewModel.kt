package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor() : ViewModel() {

    var showBottomBar = MutableStateFlow(true)
        private set

    var showSearchIcon = MutableStateFlow( true)
        private set

    var showBackIcon = MutableStateFlow(false)
        private set

    var bottomBarSelectedIndex = MutableStateFlow(0)
        private set

    var showTextTitle = MutableStateFlow(false)
        private set

    var showTopBar = MutableStateFlow(true)
        private set

    fun updateBottomBarSelectedIndex(index: Int) {
        bottomBarSelectedIndex.value = index
    }

    //SearchScreen
    fun navigatingToSearch() {
        showSearchIcon.value = false
        showBottomBar.value = false
        showBackIcon.value = true
        showTextTitle.value = true
        showTopBar.value = true
    }

    //HomeScreen and FavoriteScreen
    fun navigatingToMainScreen() {
        showSearchIcon.value = true
        showBottomBar.value = true
        showBackIcon.value = false
        showTextTitle.value = false
        showTopBar.value = true
    }

    //MovieDetailsScreen
    fun navigatingToMovieDetails() {
        showSearchIcon.value = false
        showBottomBar.value = false
        showBackIcon.value = true
        showTextTitle.value = false
        showTopBar.value = false
    }
}