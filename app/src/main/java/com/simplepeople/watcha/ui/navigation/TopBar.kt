@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.simplepeople.watcha.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.primaryContainerDark
import com.example.compose.primaryDark
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.stateholder.TopBarItemOption

//TODO: add a menu with a toggle option for changing between English and Spanish
// in order to change the string locale and implement the query for language in the API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    appBarOption: AppBarOption,
    selectedTopBarItem: TopBarItemOption,
    navigateToSearchScreen: () -> Unit,
    filterNowPlaying: () -> Unit,
    filterPopular: () -> Unit,
    filterTopRated: () -> Unit,
    filterUpcoming: () -> Unit,
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    when (appBarOption) {
        AppBarOption.HOME -> {
            HomeTopAppBar(
                navigateToSearchScreen = navigateToSearchScreen,
                scrollBehavior = scrollBehavior,
                filterNowPlaying = filterNowPlaying,
                filterPopular = filterPopular,
                filterTopRated = filterTopRated,
                filterUpcoming = filterUpcoming,
                selectedTopBarItem = selectedTopBarItem
            )
        }

        AppBarOption.FAVORITE -> {
            MainTopAppBar(
                navigateToSearchScreen = navigateToSearchScreen,
                scrollBehavior = scrollBehavior
            )
        }

        AppBarOption.SEARCH -> {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
                screenTitleResource = AppScreens.SearchScreen.name
            )
        }

        AppBarOption.MOVIE_DETAILS -> {
            TransparentTopAppBar(
                navigateBack = navigateBack
            )
        }
    }
}

//Options to use on SharedTopBar to select the right appbar per screen
//TODO: change it to Sealed Class
enum class AppBarOption {
    HOME,
    FAVORITE,
    SEARCH,
    MOVIE_DETAILS
}


//Screens in MainScreen (Home and Favorites)
@Composable
fun HomeTopAppBar(
    selectedTopBarItem: TopBarItemOption,
    navigateToSearchScreen: () -> Unit,
    filterNowPlaying: () -> Unit,
    filterPopular: () -> Unit,
    filterTopRated: () -> Unit,
    filterUpcoming: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
//        windowInsets = WindowInsets(0.dp, 16.dp, 0.dp, 0.dp),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                HomeTopAppBarButton(
                    topBarItemOption = TopBarItemOption.NOW_PLAYING,
                    selectedTopBarItem = selectedTopBarItem,
                    text = R.string.home_now_playing,
                    action = {
                        filterNowPlaying()
                    }
                )
                HomeTopAppBarButton(
                    topBarItemOption = TopBarItemOption.POPULAR,
                    selectedTopBarItem = selectedTopBarItem,
                    text = R.string.home_popular,
                    action = {
                        filterPopular()
                    }
                )
                HomeTopAppBarButton(
                    topBarItemOption = TopBarItemOption.TOP_RATED,
                    selectedTopBarItem = selectedTopBarItem,
                    text = R.string.home_top_rated,
                    action = {
                        filterTopRated()
                    }
                )
                HomeTopAppBarButton(
                    topBarItemOption = TopBarItemOption.UPCOMING,
                    selectedTopBarItem = selectedTopBarItem,
                    text = R.string.home_upcoming,
                    action = {
                        filterUpcoming()
                    }
                )
            }

        },
        navigationIcon = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_main_screen),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(120.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navigateToSearchScreen()
                },
                modifier = Modifier
                    .size(36.dp)
                    .padding(0.dp, 0.dp, 3.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = Icons.Default.Search.name,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = Color.Transparent.copy(
//                alpha = .75f
//            )
//        )
    )

}

@Composable
fun MainTopAppBar(
    navigateToSearchScreen: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Column() {
        TopAppBar(
            title = {},
            navigationIcon = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_main_screen),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .size(120.dp)
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            actions = {
                IconButton(
                    onClick = {
                        navigateToSearchScreen()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .padding(0.dp, 0.dp, 3.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = Icons.Default.Search.name,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        )
    }
}

//Screens that displays a title and back navigation, like Search
@Composable
fun SingleScreenTopAppBar(
    navigateBack: () -> Unit,
    screenTitleResource: Int,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Row {
                Text(
                    text = stringResource(id = screenTitleResource)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigateBack()
                }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    Icons.Filled.ArrowBack.name
                )
            }

        },
        scrollBehavior = scrollBehavior
    )
}

//Screens for detailed info, like MovieDetails
@Composable
fun TransparentTopAppBar(
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {
                    navigateBack()
                }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    Icons.Filled.ArrowBack.name
                )
            }

        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun HomeTopAppBarButton(
    topBarItemOption: TopBarItemOption,
    selectedTopBarItem: TopBarItemOption,
    text: Int,
    action: () -> Unit
) {
    Button(
        onClick = {
            action()
        },
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (topBarItemOption == selectedTopBarItem) primaryDark else primaryContainerDark,
            contentColor = if (topBarItemOption == selectedTopBarItem) primaryContainerDark else primaryDark,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .padding(2.dp, 0.dp, 2.dp, 0.dp)
    ) {
        Text(
            text = stringResource(id = text),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}