package com.example.github.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.domain.notification.NotificationSenderManager
import com.example.github.R
import com.example.github.core.receiver.NotificationReceiver
import com.example.github.core.util.Constants.CHANNEL_ID
import com.example.github.core.util.Constants.DISMISS_ALL_ACTION

class NotificationSenderManagerImpl(
    private val context: Context,
) : NotificationSenderManager {
    override fun sendNotification(
        title: String?,
        message: String?,
        notificationId: Int,
    ) {
        val broadcastPendingIntent = createBroadcastIntent(context = context)

        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_github)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_github,
                    ),
                )
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(broadcastPendingIntent)
                .setColorized(true)
                .setColor(Color.DKGRAY)
                .setLights(Color.GREEN, 1000, 3000)
                .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "Channel title",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    enableLights(true)
                    lightColor = Color.BLACK
                    enableVibration(true)
                }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, notification)
    }

    private fun createBroadcastIntent(context: Context): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationReceiver::class.java).apply {
                action = DISMISS_ALL_ACTION
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
        )
}
