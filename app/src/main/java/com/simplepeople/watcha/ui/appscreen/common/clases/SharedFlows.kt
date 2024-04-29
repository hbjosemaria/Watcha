package com.simplepeople.watcha.ui.appscreen.common.clases

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

//Hot flow for handling scroll to top when user touches twice the same selected bottom item on BottomBar
@Module
@InstallIn(SingletonComponent::class)
class SharedScrollToTopFlow @Inject constructor() {

    object Instance {
        val scrollToTopFlow = MutableSharedFlow<Unit>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        fun emitTopScrollEvent() {
            scrollToTopFlow.tryEmit(Unit)
        }
    }

    @Provides
    fun provideScrollToTopFlow () : Instance {
        return Instance
    }

}

//Hot flow for handling HomeScreen movie filtering with TopBar options on HomeScreen
@Module
@InstallIn(SingletonComponent::class)
class SharedHomeFilterFlow @Inject constructor() {

    object Instance {
        val homeFilterFlow = MutableSharedFlow<HomeFilterOptions>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        fun emitFilterEvent(filterOption : HomeFilterOptions) {
            homeFilterFlow.tryEmit(filterOption)
        }
    }

    @Provides
    fun provideSharedHomeFilterFlow () : Instance {
        return Instance
    }

}

sealed class HomeFilterOptions () {
    data object NowPlaying : HomeFilterOptions()
    data object Popular : HomeFilterOptions()
    data object TopRated : HomeFilterOptions()
    data object Upcoming : HomeFilterOptions()
}

//Hot flow for handling movie adding to favorite list and display a Snackbar on AppNavigation Scaffold
@Module
@InstallIn(SingletonComponent::class)
class SharedFavoriteEventFlow @Inject constructor() {

    object Instance {
        val favoriteEventFlow = MutableSharedFlow<Int>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        fun emitFavoriteEvent(stringReference : Int) {
            favoriteEventFlow.tryEmit(stringReference)
        }
    }

    @Provides
    fun provideSharedFavoriteEventFlow () : Instance {
        return Instance
    }

}