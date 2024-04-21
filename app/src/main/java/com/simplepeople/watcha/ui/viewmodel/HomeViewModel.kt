package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

//    var movieList = movieListUseCase.getMoviesWithPager().cachedIn(viewModelScope)
//        private set

    var movieList : Flow<PagingData<Movie>>

    var currentPage = MutableStateFlow(1)
        private set

    init {
        movieList = movieListUseCase.getMoviesWithPager().cachedIn(viewModelScope)
//        getFirstPage()
    }

    /*fun getFirstPage() {
        viewModelScope.launch {
            movieList.value = withContext(Dispatchers.IO) {
                movieListUseCase.getFirstPage()
            }
        }
    }

    fun getNextPage() {
        viewModelScope.launch{
            currentPage.value++
            movieList.value = movieList.value.plus(withContext(Dispatchers.IO) {
                movieListUseCase.getNextPage(currentPage.value)
            })
        }
    }*/
}