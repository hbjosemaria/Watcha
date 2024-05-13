package com.simplepeople.watcha.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.ui.navigation.NavigationBarItemSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
//    private val scrollToTopFlow: SharedScrollToTopFlow.Instance,
//    private val homeFilterFlow: SharedHomeFilterFlow.Instance
) : ViewModel() {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList = _movieList.asStateFlow()
//    val scrollToTop = mutableStateOf(false)
    val homeScreenUiState = mutableStateOf(HomeScreenUiState())

    init {
        //Movies loaded by default from Now Playing
        loadMovies(HomeFilterOptions.NowPlaying)
        //Hot flow collecting emits from bottombar to check if it has to scroll to top the list
//        viewModelScope.launch {
//            scrollToTopFlow.scrollToTopFlow.collect {
//                scrollToTop.value = true
//            }
//        }
//        //Hot flow collecting emits from topbar to filter movies at HomeScreen
//        viewModelScope.launch {
//            homeFilterFlow.homeFilterFlow.collect { filterOption ->
//                loadMovies(filterOption)
//            }
//        }
    }

    //TODO: update all Use Case calls with status management
    fun loadMovies(filterOption : HomeFilterOptions) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieListUseCase
                    .getMovies(filterOption)
                    .cachedIn(viewModelScope)
                    .onStart { }
                    .onCompletion { }
                    .catch { }
                    .collect {
                        _movieList.value = it
                    }
            }
        }
    }

    fun updateTopBarSelection(filterOption: HomeFilterOptions) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            selectedHomeFilterOption = filterOption
        )
    }

    fun scrollingToTop(scrollToTopAction : Boolean) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            scrollToTop = scrollToTopAction
        )
//        scrollToTop.value = false
    }

    fun updateNavigationItemIndex(index : Int) {
        NavigationBarItemSelection.selectedNavigationItemIndex = index
    }

}

sealed class HomeFilterOptions () {
    data object NowPlaying : HomeFilterOptions()
    data object Popular : HomeFilterOptions()
    data object TopRated : HomeFilterOptions()
    data object Upcoming : HomeFilterOptions()
}