package com.exitreminder.exitdetection

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExitDetectionApp : Application() {

    companion object {
        const val CHANNEL_ID_MONITORING = "exit_monitoring"
        const val CHANNEL_ID_ALERTS = "exit_alerts"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Monitoring channel (for foreground service)
            val monitoringChannel = NotificationChannel(
                CHANNEL_ID_MONITORING,
                "Exit Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Zeigt an, dass Exit Detection aktiv ist"
                setShowBadge(false)
            }

            // Alerts channel (for reminders)
            val alertsChannel = NotificationChannel(
                CHANNEL_ID_ALERTS,
                "Exit Alarme",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Benachrichtigungen wenn du das Gebäude verlässt"
                enableVibration(true)
                enableLights(true)
            }

            notificationManager.createNotificationChannels(
                listOf(monitoringChannel, alertsChannel)
            )
        }
    }
}
