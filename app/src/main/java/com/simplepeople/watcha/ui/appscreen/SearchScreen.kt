package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.appscreen.common.MovieAvatar
import com.simplepeople.watcha.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@FlowPreview
@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val movieSet by searchViewModel.movieSet.collectAsState()
    val textFieldText by searchViewModel.textFieldText.collectAsState()

    LaunchedEffect(textFieldText) {
        snapshotFlow {
            textFieldText
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { textFieldValue ->
                if (textFieldValue.isNotBlank()) {
                    searchViewModel.getMoviesByTitle(textFieldText)
                } else {
                    searchViewModel.cleanMovieSearch()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = textFieldText,
            onValueChange = {newText ->
                searchViewModel.onTextFieldChange(newText)
            },
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            maxLines = 1,
            label = { Text(stringResource(R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        //TODO: scroll back to top automatically when new movie data is fetched
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(
                items = movieSet.toList(),
                key = {
                    it.movieId
                }
            ) { movie ->
                MovieAvatar(
                    movie = movie,
                    navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") }
                )
            }
        }
    }
}