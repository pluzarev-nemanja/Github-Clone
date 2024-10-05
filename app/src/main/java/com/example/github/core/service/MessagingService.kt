package com.example.github.core.service

import com.example.domain.notification.NotificationSenderManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class MessagingService : FirebaseMessagingService() {
    private val notificationSenderManager by inject<NotificationSenderManager>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { notification ->
            notificationSenderManager.sendNotification(
                title = notification.title,
                message = notification.body,
                notificationId = message.hashCode(),
            )
        }
    }
}
