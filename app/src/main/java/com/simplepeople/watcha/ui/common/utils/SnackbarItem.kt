package com.simplepeople.watcha.ui.common.utils

import com.simplepeople.watcha.R

data class SnackbarItem(
    var show: Boolean = false,
    var isError: Boolean = false,
    var message: Int = R.string.empty_text,
)