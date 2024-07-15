package com.simplepeople.watcha

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.simplepeople.watcha.data.worker.HomeMovieCachingWorkerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WatchaApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var homeMovieCachingWorkerScheduler: HomeMovieCachingWorkerScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        homeMovieCachingWorkerScheduler.scheduleDailyFetchingWork()
    }
}

