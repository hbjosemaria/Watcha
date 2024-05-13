package com.simplepeople.watcha.ui.search

data class SearchScreenUiState (
    val searchText : String = "",
    val searching : Boolean = false,
    val scrollToTop : Boolean = false
)