package com.exitreminder.exitdetection.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.exitreminder.exitdetection.domain.model.ExitEvent
import com.exitreminder.exitdetection.domain.model.ExitEventType
import com.exitreminder.exitdetection.domain.model.FalseAlarmReason
import com.exitreminder.exitdetection.domain.model.FalseAlarmReport
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderActionReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ReminderActionReceiver"
        private const val ALERT_NOTIFICATION_ID = 1000
    }

    @Inject
    lateinit var repository: ExitRepository

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("reminder_id", -1)
        if (reminderId == -1L) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ALERT_NOTIFICATION_ID + reminderId.toInt())

        when (intent.action) {
            "ACTION_DISMISS" -> {
                Log.d(TAG, "Reminder $reminderId dismissed")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.logEvent(
                        reminderId = reminderId,
                        event = ExitEvent(
                            type = ExitEventType.REMINDER_DISMISSED,
                            title = "Reminder bestätigt",
                            description = "Nutzer hat den Reminder als erledigt markiert"
                        )
                    )
                }
            }

            "ACTION_FALSE_ALARM" -> {
                Log.d(TAG, "Reminder $reminderId marked as false alarm")
                CoroutineScope(Dispatchers.IO).launch {
                    val report = FalseAlarmReport(
                        reminderId = reminderId,
                        reason = FalseAlarmReason.STILL_INSIDE,
                        userComment = null
                    )
                    repository.reportFalseAlarm(report)

                    repository.logEvent(
                        reminderId = reminderId,
                        event = ExitEvent(
                            type = ExitEventType.FALSE_ALARM,
                            title = "Fehlalarm gemeldet",
                            description = "Nutzer war noch im Gebäude"
                        )
                    )
                }
            }
        }
    }
}
