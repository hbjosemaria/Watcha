package com.simplepeople.watcha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.simplepeople.watcha.ui.appnavigation.AppNavigation
import com.simplepeople.watcha.ui.theme.WatchaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.hide() //TODO: remove this when I can fix the Style NoActionBar bug
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //TODO: add here loading conditions before HomeScreen appears
        // splashScreen.setKeepOnScreenCondition(SplashScreen.KeepOnScreenCondition {  })

        setContent {
            WatchaTheme {
                AppNavigation()
            }
        }
    }
}