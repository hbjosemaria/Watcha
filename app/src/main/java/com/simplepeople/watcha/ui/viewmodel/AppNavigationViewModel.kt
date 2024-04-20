package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor() : ViewModel() {

    var showBottomBar = MutableStateFlow(true)
        private set

    var showTopBar = MutableStateFlow(true)
        private set

    var showSearchIcon = MutableStateFlow( true)
        private set

    var showBackIcon = MutableStateFlow(false)
        private set

    var bottomBarSelectedIndex = MutableStateFlow(0)
        private set

    fun updateBottomBarSelectedIndex(index: Int) {
        bottomBarSelectedIndex.value = index
    }

    fun toggleShoWSearchIcon(status: Boolean) {
        showSearchIcon.value = status
    }

    fun toggleShowTopBar(status: Boolean) {
        showTopBar.value = status
    }

    fun toggleShowBottomBar(status: Boolean) {
        showBottomBar.value = status
    }

    fun toggleShowBackIcon(status: Boolean) {
        showBackIcon.value = status
    }
}