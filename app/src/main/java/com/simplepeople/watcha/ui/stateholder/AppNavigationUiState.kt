package com.simplepeople.watcha.ui.stateholder

import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.AppBarOption

data class AppNavigationUiState (
    var showBottomBar : Boolean = true,
    var appBarOption: AppBarOption = AppBarOption.HOME,
    var scrollToTop : Boolean = false,
    var selectedBottomItemIndex : Int = 0,
    var screenTitle : Int = 0,
)

data class SnackBarItem (
    var showSnackbar : Boolean = false,
    var textSnackbar : Int = R.string.empty_text
)