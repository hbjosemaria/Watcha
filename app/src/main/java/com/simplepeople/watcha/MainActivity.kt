package com.simplepeople.watcha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.simplepeople.watcha.ui.navigation.AppNavigation
import com.simplepeople.watcha.ui.theme.WatchaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.hide()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WatchaTheme {
                AppNavigation()
            }
        }
    }
}