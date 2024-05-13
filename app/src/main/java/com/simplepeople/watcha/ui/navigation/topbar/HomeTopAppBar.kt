@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.simplepeople.watcha.ui.navigation.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.home.HomeFilterOptions
import com.simplepeople.watcha.ui.navigation.topbar.common.TopAppBarButton
import com.simplepeople.watcha.ui.navigation.topbar.common.TopAppBarLogo

//HomeScreen TopBar
@Composable
fun HomeTopAppBar(
    selectedHomeFilterOption: HomeFilterOptions,
    navigateToSearchScreen: () -> Unit,
    filterNowPlaying: () -> Unit,
    filterPopular: () -> Unit,
    filterTopRated: () -> Unit,
    filterUpcoming: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    topBarAlpha: Float? = null
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = topBarAlpha ?: 1f
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
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.Popular,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_popular,
                    action = {
                        filterPopular()
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.TopRated,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_top_rated,
                    action = {
                        filterTopRated()
                    }
                )
                TopAppBarButton(
                    buttonFilterOption = HomeFilterOptions.Upcoming,
                    selectedFilterOption = selectedHomeFilterOption,
                    text = R.string.home_upcoming,
                    action = {
                        filterUpcoming()
                    }
                )
            }
        },
        navigationIcon = {
            TopAppBarLogo()
        },
        actions = {
            DefaultIconButton(
                action = navigateToSearchScreen,
                iconImage = Icons.Default.Search,
                contentDescription = Icons.Default.Search.name,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    )

}