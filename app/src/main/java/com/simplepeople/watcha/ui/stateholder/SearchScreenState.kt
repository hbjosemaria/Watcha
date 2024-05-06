package com.simplepeople.watcha.ui.stateholder

data class SearchScreenUiState (
    val searchText : String = "",
    val searching : Boolean = false,
    val scrollToTop : Boolean = false
)