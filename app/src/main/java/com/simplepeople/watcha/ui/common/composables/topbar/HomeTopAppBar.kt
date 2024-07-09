@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarButton
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarLogo

/**
 * TopBar exclusive for Home
 */
@Composable
fun HomeTopAppBar(
    selectedHomeFilterOption: HomeFilterOptions,
    filterNowPlaying: () -> Unit,
    filterPopular: () -> Unit,
    filterTopRated: () -> Unit,
    filterUpcoming: () -> Unit,
    scrollToTop: () -> Unit,
    loadMovies: () -> Unit,
    navigateToSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background.copy(
                alpha = .5f
            )
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.NowPlaying,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_now_playing,
                    action = {
                        filterNowPlaying()
                        loadMovies()
                        scrollToTop()
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.Popular,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_popular,
                    action = {
                        filterPopular()
                        loadMovies()
                        scrollToTop()
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.TopRated,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_top_rated,
                    action = {
                        filterTopRated()
                        loadMovies()
                        scrollToTop()
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.Upcoming,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_upcoming,
                    action = {
                        filterUpcoming()
                        loadMovies()
                        scrollToTop()
                    }
                )
            }
        },
        navigationIcon = {
            TopAppBarLogo()
        },
        actions = {
            DefaultIconButton(
                action = navigateToSettings,
                iconImage = Icons.Default.Settings,
                contentDescription = Icons.Default.Settings.name,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 8.dp)
            )
        }
    )
}

sealed class HomeFilterOptions(val categoryId: Int) {
    data object NowPlaying : HomeFilterOptions(categoryId = 1)
    data object Popular : HomeFilterOptions(categoryId = 2)
    data object TopRated : HomeFilterOptions(categoryId = 3)
    data object Upcoming : HomeFilterOptions(categoryId = 4)
}