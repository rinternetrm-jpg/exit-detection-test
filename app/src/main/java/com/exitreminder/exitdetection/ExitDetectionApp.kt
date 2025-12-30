package com.exitreminder.exitdetection

import android.app.Application
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
        Log.d(TAG, "Application onCreate with Hilt")
    }
}
