package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    var movieList = MutableStateFlow<List<Movie>>(listOf())
        private set

    init {
        getFavorites()
    }

    fun getFavorites() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteUseCase.getFavorites().collect {
                    movieList.value = it
                }
            }
        }
    }

}