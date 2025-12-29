package com.exitreminder.exitdetection.presentation.screens.livetest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.exitreminder.exitdetection.service.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveTestViewModel @Inject constructor(
    private val repository: ExitRepository,
    private val wifiService: WifiService,
    private val locationService: LocationService,
    private val stepCounterService: StepCounterService,
    private val barometerService: BarometerService,
    private val lightSensorService: LightSensorService,
    private val exitPredictor: ExitPredictor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val reminderId: Long = savedStateHandle.get<Long>("reminderId") ?: 0L

    private val _reminder = MutableStateFlow<Reminder?>(null)
    val reminder: StateFlow<Reminder?> = _reminder.asStateFlow()

    private val _sensorData = MutableStateFlow(LiveSensorData())
    val sensorData: StateFlow<LiveSensorData> = _sensorData.asStateFlow()

    private val _prediction = MutableStateFlow(ExitPrediction(exitProbability = 0f))
    val prediction: StateFlow<ExitPrediction> = _prediction.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration: StateFlow<Long> = _recordingDuration.asStateFlow()

    private var monitoringJob: Job? = null
    private var durationJob: Job? = null
    private var startTime: Long = 0

    init {
        loadReminder()
    }

    private fun loadReminder() {
        viewModelScope.launch {
            _reminder.value = repository.getReminderById(reminderId)
        }
    }

    fun startMonitoring() {
        _isRecording.value = true
        startTime = System.currentTimeMillis()

        // Start all sensors
        locationService.startLocationUpdates()
        stepCounterService.startCounting()
        barometerService.startMeasuring()
        lightSensorService.startMeasuring()

        // Start monitoring loop
        monitoringJob = viewModelScope.launch {
            while (true) {
                updateSensorData()
                updatePrediction()
                delay(1000)  // Update every second
            }
        }

        // Start duration counter
        durationJob = viewModelScope.launch {
            while (true) {
                _recordingDuration.value = (System.currentTimeMillis() - startTime) / 1000
                delay(1000)
            }
        }
    }

    fun stopMonitoring() {
        _isRecording.value = false
        monitoringJob?.cancel()
        durationJob?.cancel()

        locationService.stopLocationUpdates()
        stepCounterService.stopCounting()
        barometerService.stopMeasuring()
        lightSensorService.stopMeasuring()
    }

    private fun updateSensorData() {
        wifiService.updateWifiState()

        val wifi = wifiService.wifiState.value
        val location = locationService.locationState.value
        val altitude = barometerService.altitudeState.value
        val light = lightSensorService.lightState.value
        val steps = stepCounterService.steps.value
        val stepsSinceLastCheck = stepCounterService.getStepsSinceLastCheck()

        val profile = _reminder.value?.profile

        // Calculate distance to street
        val distanceToStreet = if (profile != null) {
            // Simple estimate based on profile
            profile.nearestStreetDistance - location.distanceFromStart
        } else {
            0f
        }.coerceAtLeast(0f)

        // Determine direction
        val currentDirection = Direction.fromBearing(location.bearing)
        val movingTowardsStreet = profile?.let {
            currentDirection == it.nearestStreetDirection
        } ?: false

        _sensorData.value = LiveSensorData(
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
            distanceToStreet = distanceToStreet,
            distanceToNearestExit = distanceToStreet,
            movingTowardsStreet = movingTowardsStreet,
            movingTowardsExit = movingTowardsStreet,
            currentDirection = currentDirection
        )
    }

    private fun updatePrediction() {
        val profile = _reminder.value?.profile ?: return
        val data = _sensorData.value

        _prediction.value = exitPredictor.calculateExitProbability(profile, data)
    }

    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }
}
