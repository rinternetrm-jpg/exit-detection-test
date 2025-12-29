package com.exitreminder.exitdetection.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.exitreminder.exitdetection.service.ExitMonitorService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    @Inject
    lateinit var repository: ExitRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed, checking for active reminders")

            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val activeReminders = repository.observeActiveReminders().first()
                    if (activeReminders.isNotEmpty()) {
                        Log.d(TAG, "Found ${activeReminders.size} active reminders, starting service")
                        ExitMonitorService.start(context)
                    } else {
                        Log.d(TAG, "No active reminders, service not started")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error checking reminders", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
