package net.android.app.flickr

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.hilt.android.HiltAndroidApp
import net.android.app.flickr.data.local.preferences.PreferencesRepository
import net.android.app.flickr.data.remote.service.FlickrAPI
import net.android.app.flickr.data.worker.PollWorker
import net.android.app.flickr.domain.repository.FlickrRepository
import java.util.logging.Level
import javax.inject.Inject

const val NOTIFICATION_CHANNEL_ID = "flickr_poll"

@HiltAndroidApp
class FlickrApp :  Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)

        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @Inject
    lateinit var pollWorker: PollWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(pollWorker)
            .build()

    class PollWorkerFactory @Inject constructor(
        private val remoteRepository: FlickrRepository,
        private val preferencesRepository: PreferencesRepository,
        private val flickrAPI: FlickrAPI
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return PollWorker(
                appContext,
                workerParameters,
                flickrAPI,
                preferencesRepository
            )
        }
    }
}