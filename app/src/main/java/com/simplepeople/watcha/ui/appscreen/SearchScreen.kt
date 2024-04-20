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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
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
    var textFieldState by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(textFieldState) {
        snapshotFlow {
            textFieldState
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { textFieldValue ->
                if (textFieldValue.text.isNotBlank()) {
                    searchViewModel.getMoviesByTitle(textFieldValue.text)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = textFieldState,
            onValueChange = {
                textFieldState = it
            },
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            maxLines = 1,
            label = { Text(stringResource(R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(movieSet.toList()) { movie ->
                MovieAvatar(
                    movie = movie,
                    navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") }
                )
            }
        }
    }
}