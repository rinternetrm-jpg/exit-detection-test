package com.exitreminder.exitdetection

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExitDetectionApp : Application() {

    companion object {
        private const val TAG = "ExitDetectionApp"
        const val CHANNEL_ID_MONITORING = "exit_monitoring"
        const val CHANNEL_ID_ALERTS = "exit_alerts"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application onCreate started")
        try {
            createNotificationChannels()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating notification channels", e)
        }
        Log.d(TAG, "Application onCreate completed")
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
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

                notificationManager?.createNotificationChannels(
                    listOf(monitoringChannel, alertsChannel)
                )
                Log.d(TAG, "Notification channels created successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error in createNotificationChannels", e)
            }
        }
    }
}
