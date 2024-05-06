package com.simplepeople.watcha.ui.stateholder

import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.AppBarOption

data class AppNavigationUiState (
    var showNavigationBar : Boolean = true,
    var appBarOption: AppBarOption = AppBarOption.HOME,
    var scrollToTop : Boolean = false,
    var selectedNavigationItemIndex : Int = 0,
    var screenTitle : Int = 0,
    var selectedTopBarItem : TopBarItemOption = TopBarItemOption.NOW_PLAYING
)

enum class TopBarItemOption {
    NOW_PLAYING,
    POPULAR,
    TOP_RATED,
    UPCOMING
}

data class SnackBarItem (
    var showSnackbar : Boolean = false,
    var textSnackbar : Int = R.string.empty_text
)