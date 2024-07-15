package com.simplepeople.watcha

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.compose.WatchaTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.simplepeople.watcha.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WatchaTheme(
                darkTheme = true
            ) {
                AppNavigation(
                    isUserSignedIn = Firebase.auth.currentUser != null
                )
            }
        }
    }
}