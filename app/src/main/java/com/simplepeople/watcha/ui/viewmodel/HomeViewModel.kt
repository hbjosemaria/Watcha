package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.ui.appscreen.common.clases.HomeFilterOptions
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedHomeFilterFlow
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedScrollToTopFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val scrollToTopFlow: SharedScrollToTopFlow.Instance,
    private val homeFilterFlow: SharedHomeFilterFlow.Instance
) : ViewModel() {

    var movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
        private set

    var scrollToTop = mutableStateOf(false)
        private set

    init {
        //Movies loaded by default from Now Playing
        loadMovies(HomeFilterOptions.NowPlaying)
        //Hot flow collecting emits from bottombar to check if it has to scroll to top the list
        viewModelScope.launch {
            scrollToTopFlow.scrollToTopFlow.collect {
                scrollToTop.value = true
            }
        }
        //Hot flow collecting emits from topbar to filter movies at HomeScreen
        viewModelScope.launch {
            homeFilterFlow.homeFilterFlow.collect { filterOption ->
                loadMovies(filterOption)
            }
        }
    }

    //TODO: update all Use Case calls with status management
    private fun loadMovies(filterOption : HomeFilterOptions) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieListUseCase
                    .getMovies(filterOption)
                    .cachedIn(viewModelScope)
                    .onStart { }
                    .onCompletion { }
                    .catch { }
                    .collect {
                        movieList.value = it
                    }
            }
        }
    }

    fun resetScrollToTop() {
        scrollToTop.value = false
    }

}