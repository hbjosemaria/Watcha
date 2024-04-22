@file:OptIn(ExperimentalFoundationApi::class, FlowPreview::class)

package com.simplepeople.watcha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.simplepeople.watcha.ui.navigation.AppNavigation
import com.simplepeople.watcha.ui.theme.WatchaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.hide() //TODO: remove this when I can fix the Style NoActionBar bug
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //TODO: add here loading conditions before HomeScreen disappears
        // splashScreen.setKeepOnScreenCondition(SplashScreen.KeepOnScreenCondition {  })

        setContent {
            WatchaTheme {
                AppNavigation()
            }
        }
    }
}