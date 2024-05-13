package com.simplepeople.watcha.ui.home

data class HomeScreenUiState (
    var scrollToTop : Boolean = false,
//    var selectedNavigationItemIndex : Int = 0,
    var selectedHomeFilterOption : HomeFilterOptions = HomeFilterOptions.NowPlaying
)