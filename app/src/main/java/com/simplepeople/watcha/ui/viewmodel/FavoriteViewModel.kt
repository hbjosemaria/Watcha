package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.ui.appscreen.common.clases.SharedScrollToTopFlow
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

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val scrollToTopFlow: SharedScrollToTopFlow.Instance
) : ViewModel() {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList = _movieList.asStateFlow()
    val scrollToTop = mutableStateOf(false)

    init {
        getFavorites()
        viewModelScope.launch {
            scrollToTopFlow.scrollToTopFlow.collect {
                scrollToTop.value = true
            }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteUseCase
                    .getFavorites()
                    .cachedIn(viewModelScope)
                    .onStart {  }
                    .onCompletion {  }
                    .catch {  }
                    .collect {
                        _movieList.value = it
                    }
            }
        }
    }

    fun resetScrollToTop() {
        scrollToTop.value = false
    }

}