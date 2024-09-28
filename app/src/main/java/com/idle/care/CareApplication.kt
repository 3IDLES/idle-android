package com.idle.care

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.idle.care.notification.NotificationHandler.Companion.BACKGROUND_CHANNEL
import com.idle.care.notification.NotificationHandler.Companion.BACKGROUND_DESCRIPTION
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val channel =
            NotificationChannel(
                BACKGROUND_CHANNEL,
                BACKGROUND_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        channel.description = BACKGROUND_DESCRIPTION

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}