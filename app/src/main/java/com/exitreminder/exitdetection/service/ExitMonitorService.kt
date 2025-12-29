package com.exitreminder.exitdetection.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.exitreminder.exitdetection.ExitDetectionApp
import com.exitreminder.exitdetection.MainActivity
import com.exitreminder.exitdetection.R
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.exitreminder.exitdetection.receiver.ReminderActionReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class ExitMonitorService : Service() {

    companion object {
        private const val TAG = "ExitMonitorService"
        private const val NOTIFICATION_ID = 1
        private const val ALERT_NOTIFICATION_ID = 1000

        fun start(context: Context) {
            val intent = Intent(context, ExitMonitorService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, ExitMonitorService::class.java)
            context.stopService(intent)
        }
    }

    @Inject lateinit var repository: ExitRepository
    @Inject lateinit var wifiService: WifiService
    @Inject lateinit var locationService: LocationService
    @Inject lateinit var stepCounterService: StepCounterService
    @Inject lateinit var barometerService: BarometerService
    @Inject lateinit var lightSensorService: LightSensorService
    @Inject lateinit var exitPredictor: ExitPredictor

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var monitoringJob: Job? = null

    // Track which reminders have already triggered
    private val triggeredReminders = mutableSetOf<Long>()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        startForeground(NOTIFICATION_ID, createNotification(0))
        startMonitoring()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        stopMonitoring()
        serviceScope.cancel()
    }

    private fun startMonitoring() {
        // Start all sensors
        locationService.startLocationUpdates()
        stepCounterService.startCounting()
        barometerService.startMeasuring()
        lightSensorService.startMeasuring()

        // Collect active reminders and monitor
        monitoringJob = serviceScope.launch {
            var currentReminders = emptyList<Reminder>()

            // Launch collector for reminder updates
            launch {
                repository.observeActiveReminders().collect { reminders ->
                    Log.d(TAG, "Monitoring ${reminders.size} active reminders")
                    updateNotification(reminders.size)
                    currentReminders = reminders

                    if (reminders.isEmpty()) {
                        stopSelf()
                    }
                }
            }

            // Monitoring loop
            while (true) {
                delay(5000) // Check every 5 seconds

                if (currentReminders.isEmpty()) continue

                // Update sensor data
                wifiService.updateWifiState()
                val sensorData = collectSensorData()

                for (reminder in currentReminders) {
                    if (triggeredReminders.contains(reminder.id)) {
                        continue
                    }

                    val profile = reminder.profile ?: continue
                    val prediction = exitPredictor.calculateExitProbability(profile, sensorData)

                    Log.d(TAG, "Reminder ${reminder.id}: ${prediction.exitProbabilityPercent}% exit probability")

                    if (prediction.shouldTrigger) {
                        Log.i(TAG, "TRIGGERING reminder: ${reminder.name}")
                        triggerReminder(reminder, prediction)
                        triggeredReminders.add(reminder.id)
                    }
                }
            }
        }
    }

    private fun collectSensorData(): LiveSensorData {
        val wifi = wifiService.wifiState.value
        val location = locationService.locationState.value
        val altitude = barometerService.altitudeState.value
        val light = lightSensorService.lightState.value
        val steps = stepCounterService.steps.value
        val stepsSinceLastCheck = stepCounterService.getStepsSinceLastCheck()

        val currentDirection = Direction.fromBearing(location.bearing)

        return LiveSensorData(
            wifiConnected = wifi.isConnected,
            wifiSsid = wifi.ssid,
            wifiSignal = wifi.rssi,
            wifiSignalPercent = wifi.signalPercent,
            wifiSignalTrend = wifi.trend,
            latitude = location.latitude,
            longitude = location.longitude,
            gpsAccuracy = location.accuracy,
            gpsSpeed = location.speed,
            gpsBearing = location.bearing,
            steps = steps,
            stepsSinceLastCheck = stepsSinceLastCheck,
            isWalking = location.isWalking || stepsSinceLastCheck > 3,
            isRunning = location.isRunning,
            walkingDirection = if (stepsSinceLastCheck > 3) currentDirection else null,
            altitude = altitude.altitude.toDouble(),
            altitudeChange = altitude.altitudeChange,
            estimatedFloor = altitude.estimatedFloor,
            floorChange = altitude.floorChange,
            stairsDetected = altitude.stairsDetected,
            elevatorDetected = altitude.elevatorDetected,
            lightLevel = light.lux,
            lightTrend = light.trend,
            distanceFromStart = location.distanceFromStart,
            distanceToStreet = 0f, // Would need profile data
            distanceToNearestExit = 0f,
            movingTowardsStreet = false,
            movingTowardsExit = false,
            currentDirection = currentDirection
        )
    }

    private fun triggerReminder(reminder: Reminder, prediction: ExitPrediction) {
        // Show alert notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, ExitDetectionApp.CHANNEL_ID_ALERTS)
            .setSmallIcon(R.drawable.ic_exit)
            .setContentTitle("Exit erkannt: ${reminder.name}")
            .setContentText(reminder.reminderText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminder.reminderText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_check,
                "Erledigt",
                createDismissIntent(reminder.id)
            )
            .addAction(
                R.drawable.ic_warning,
                "Fehlalarm",
                createFalseAlarmIntent(reminder.id)
            )
            .build()

        notificationManager.notify(ALERT_NOTIFICATION_ID + reminder.id.toInt(), notification)

        // Log event
        serviceScope.launch {
            repository.logEvent(
                reminderId = reminder.id,
                event = ExitEvent(
                    type = ExitEventType.EXIT_DETECTED,
                    title = "Exit erkannt",
                    description = "Wahrscheinlichkeit: ${prediction.exitProbabilityPercent}%",
                    data = mapOf("probability" to prediction.exitProbabilityPercent.toString())
                )
            )
        }
    }

    private fun createDismissIntent(reminderId: Long): PendingIntent {
        val intent = Intent(this, ReminderActionReceiver::class.java).apply {
            action = "ACTION_DISMISS"
            putExtra("reminder_id", reminderId)
        }
        return PendingIntent.getBroadcast(
            this,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createFalseAlarmIntent(reminderId: Long): PendingIntent {
        val intent = Intent(this, ReminderActionReceiver::class.java).apply {
            action = "ACTION_FALSE_ALARM"
            putExtra("reminder_id", reminderId)
        }
        return PendingIntent.getBroadcast(
            this,
            reminderId.toInt() + 10000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun stopMonitoring() {
        monitoringJob?.cancel()
        locationService.stopLocationUpdates()
        stepCounterService.stopCounting()
        barometerService.stopMeasuring()
        lightSensorService.stopMeasuring()
    }

    private fun createNotification(reminderCount: Int): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, ExitDetectionApp.CHANNEL_ID_MONITORING)
            .setSmallIcon(R.drawable.ic_monitoring)
            .setContentTitle("Exit Detection aktiv")
            .setContentText("Überwache $reminderCount Reminder")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(reminderCount: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification(reminderCount))
    }
}
