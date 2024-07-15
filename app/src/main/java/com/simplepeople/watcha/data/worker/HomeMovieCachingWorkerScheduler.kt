package com.simplepeople.watcha.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeMovieCachingWorkerScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val workName = "DailyHomeMovieCachingWork"
    private val timeInterval = 24L

    fun scheduleDailyFetchingWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dailyWorkRequest =
            PeriodicWorkRequestBuilder<HomeMovieCachingWorker>(timeInterval, TimeUnit.HOURS)
                .setInitialDelay(calculateInitialDelayUntil3AM(), TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                workName,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )
    }

    private fun calculateInitialDelayUntil3AM(): Long {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val targetTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            if (get(Calendar.HOUR_OF_DAY) >= 3) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return targetTime.timeInMillis - currentTime.timeInMillis
    }
}