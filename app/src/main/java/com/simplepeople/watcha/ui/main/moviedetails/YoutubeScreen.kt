package com.simplepeople.watcha.ui.main.moviedetails

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun YoutubeScreen(
    videoKey: String,
    navigateBack: () -> Unit,
) {

    val activity = LocalContext.current as Activity
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    val view = LocalView.current
    val insetsController = WindowCompat.getInsetsController(activity.window, view).apply {
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    BackHandler {
        navigateBack()
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = { context ->
                val youTubePlayerView = YouTubePlayerView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    enableAutomaticInitialization = false

                }

                val iFramePlayerOptions = IFramePlayerOptions.Builder()
                    .controls(1)
                    .build()

                youTubePlayerView.initialize(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, videoKey, 0f)
                        }
                    },
                    iFramePlayerOptions
                )
                youTubePlayerView
            }
        )
    }


}