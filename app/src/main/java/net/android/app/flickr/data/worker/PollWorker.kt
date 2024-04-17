package net.android.app.flickr.data.worker

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import net.android.app.flickr.NOTIFICATION_CHANNEL_ID
import net.android.app.flickr.R
import net.android.app.flickr.data.local.preferences.PreferencesRepository
import net.android.app.flickr.data.remote.mappers.toFlickrImage
import net.android.app.flickr.data.remote.service.FlickrAPI
import net.android.app.flickr.presentation.MainActivity

@HiltWorker
class PollWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    @Assisted private val flickrAPI: FlickrAPI,
    @Assisted private val preferencesRepository: PreferencesRepository
): CoroutineWorker(
    context,
    workerParameters
) {
    override suspend fun doWork(): Result {
        try {

            val storedQuery = preferencesRepository.storedQuery.first()
            val lastResultId = preferencesRepository.lastResultId.first()

            if ( storedQuery.isEmpty() ) {
                Log.d("KOTLIN", "No search query stored in preferences,so work is finished early")
                return Result.success()
            }

            val photos = flickrAPI.searchPhotos(storedQuery, 1, 50)
                .photos.photosList.map { it.toFlickrImage() }

            if ( photos.isNotEmpty() ) {
                val newResultId = photos[0].id
                if ( newResultId == lastResultId ) {
                    Log.i("KOTLIN", "Still have the same result: $newResultId")
                } else {
                    Log.i("KOTLIN", "Got a new result: $newResultId")
                    notifyUser()
                    preferencesRepository.setLastResultId(newResultId)
                }
            }

            return Result.success()

        }catch (ex: Exception) {
            Log.d("KOTLIN", "Error in the PollWorker $ex")
            return Result.failure()
        }
    }

    private fun notifyUser() {
        val intent = MainActivity.callNewIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val resources = context.resources

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_pictures_title))
            .setColorized(true)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentText(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val isNotificationPermGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //for android 13 and above
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS ) == PackageManager.PERMISSION_GRANTED
        } else {
            //For versions before TIRAMISU, no specific permission is needed for notifications
            true
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        if ( !isNotificationPermGranted ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                256
            )
            return
        }

        NotificationManagerCompat.from(context).notify(0, notification)
    }

}