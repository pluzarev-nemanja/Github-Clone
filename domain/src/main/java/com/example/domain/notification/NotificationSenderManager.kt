package com.example.domain.notification

interface NotificationSenderManager {
    fun sendNotification(
        title: String?,
        message: String?,
        notificationId: Int,
    )
}
