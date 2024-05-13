package com.simplepeople.watcha.ui.moviedetails

import com.simplepeople.watcha.ui.common.utils.SnackbarItem

data class MovieDetailsUiState (
    var snackBarItem: SnackbarItem = SnackbarItem(),
    var rating : Float = 0f
)