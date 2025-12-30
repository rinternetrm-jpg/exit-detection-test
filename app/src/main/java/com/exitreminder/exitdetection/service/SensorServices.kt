package com.exitreminder.exitdetection.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.exitreminder.exitdetection.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

/**
 * WiFi signal monitoring service
 */
@Singleton
class WifiService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val wifiManager: WifiManager? = try {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    } catch (e: Exception) {
        null
    }

    private val _wifiState = MutableStateFlow(WifiState())
    val wifiState: StateFlow<WifiState> = _wifiState.asStateFlow()

    private var previousSignal: Int = -100

    fun updateWifiState() {
        try {
            val info = wifiManager?.connectionInfo
            val isConnected = info != null && info.networkId != -1
            val ssid = if (isConnected) info?.ssid?.removeSurrounding("\"") ?: "" else ""
            val bssid = if (isConnected) info?.bssid ?: "" else ""
            val rssi = if (isConnected) info?.rssi ?: -100 else -100

            val trend = when {
                rssi > previousSignal + 3 -> Trend.RISING
                rssi < previousSignal - 3 -> Trend.FALLING
                else -> Trend.STABLE
            }
            previousSignal = rssi

            _wifiState.value = WifiState(
                isConnected = isConnected,
                ssid = ssid,
                bssid = bssid,
                rssi = rssi,
                signalPercent = LiveSensorData.dbmToPercent(rssi),
                trend = trend
            )
        } catch (e: Exception) {
            // Permission not granted or other error
            _wifiState.value = WifiState()
        }
    }

    fun getCurrentSsid(): String? {
        return try {
            val info = wifiManager?.connectionInfo
            if (info?.networkId != -1) {
                info?.ssid?.removeSurrounding("\"")
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

data class WifiState(
    val isConnected: Boolean = false,
    val ssid: String = "",
    val bssid: String = "",
    val rssi: Int = -100,
    val signalPercent: Int = 0,
    val trend: Trend = Trend.STABLE
)

/**
 * GPS/Location service using Fused Location Provider
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    private var startLocation: Location? = null
    private var previousLocation: Location? = null

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000  // 5 seconds
    ).setMinUpdateIntervalMillis(2000).build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                updateLocation(location)
            }
        }
    }

    fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            // Handle permission not granted
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocation(location: Location) {
        if (startLocation == null) {
            startLocation = location
        }

        val distanceFromStart = startLocation?.distanceTo(location) ?: 0f
        val speed = location.speed

        _locationState.value = LocationState(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            altitude = location.altitude,
            speed = speed,
            bearing = location.bearing,
            distanceFromStart = distanceFromStart,
            isWalking = speed > 0.5f && speed < 2.5f,
            isRunning = speed >= 2.5f
        )

        previousLocation = location
    }

    fun resetStartLocation() {
        startLocation = null
    }

    suspend fun getCurrentLocation(): Location? {
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitResult(): T? {
        return try {
            this.await()
        } catch (e: Exception) {
            null
        }
    }
}

data class LocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 100f,
    val altitude: Double = 0.0,
    val speed: Float = 0f,
    val bearing: Float = 0f,
    val distanceFromStart: Float = 0f,
    val isWalking: Boolean = false,
    val isRunning: Boolean = false
)

/**
 * Step counter service
 */
@Singleton
class StepCounterService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sensorManager: SensorManager? = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Exception) {
        null
    }
    private val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val stepDetector = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps.asStateFlow()

    private var initialSteps: Int = -1
    private var lastCheckSteps: Int = 0
    private var lastCheckTime: Long = 0

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    val totalSteps = event.values[0].toInt()
                    if (initialSteps < 0) {
                        initialSteps = totalSteps
                    }
                    _steps.value = totalSteps - initialSteps
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    _steps.value = _steps.value + 1
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun startCounting() {
        initialSteps = -1
        _steps.value = 0
        lastCheckSteps = 0
        lastCheckTime = System.currentTimeMillis()

        try {
            stepSensor?.let {
                sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
            } ?: stepDetector?.let {
                sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        } catch (e: Exception) {
            // Sensor not available
        }
    }

    fun stopCounting() {
        try {
            sensorManager?.unregisterListener(listener)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun getStepsSinceLastCheck(): Int {
        val currentSteps = _steps.value
        val stepsDiff = currentSteps - lastCheckSteps
        lastCheckSteps = currentSteps
        lastCheckTime = System.currentTimeMillis()
        return stepsDiff
    }

    fun reset() {
        initialSteps = -1
        _steps.value = 0
        lastCheckSteps = 0
    }
}

/**
 * Barometer/Altitude service
 */
@Singleton
class BarometerService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sensorManager: SensorManager? = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Exception) {
        null
    }
    private val pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)

    private val _altitudeState = MutableStateFlow(AltitudeState())
    val altitudeState: StateFlow<AltitudeState> = _altitudeState.asStateFlow()

    private var baseAltitude: Float? = null
    private var previousAltitude: Float = 0f

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val pressure = event.values[0]
            val altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)

            if (baseAltitude == null) {
                baseAltitude = altitude
            }

            val change = altitude - (baseAltitude ?: altitude)
            val floorChange = when {
                change > 2.5f -> FloorChange.UP
                change < -2.5f -> FloorChange.DOWN
                else -> FloorChange.SAME
            }

            val estimatedFloor = (change / 3.0f).roundToInt()  // ~3m per floor

            // Detect stairs (continuous altitude change)
            val stairsDetected = abs(altitude - previousAltitude) > 0.3f &&
                    abs(altitude - previousAltitude) < 1.0f

            // Detect elevator (rapid altitude change)
            val elevatorDetected = abs(altitude - previousAltitude) > 1.0f

            _altitudeState.value = AltitudeState(
                altitude = altitude,
                altitudeChange = change,
                estimatedFloor = estimatedFloor,
                floorChange = floorChange,
                stairsDetected = stairsDetected,
                elevatorDetected = elevatorDetected
            )

            previousAltitude = altitude
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun startMeasuring() {
        baseAltitude = null
        try {
            pressureSensor?.let {
                sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        } catch (e: Exception) {
            // Sensor not available
        }
    }

    fun stopMeasuring() {
        try {
            sensorManager?.unregisterListener(listener)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun reset() {
        baseAltitude = null
    }
}

data class AltitudeState(
    val altitude: Float = 0f,
    val altitudeChange: Float = 0f,
    val estimatedFloor: Int = 0,
    val floorChange: FloorChange = FloorChange.SAME,
    val stairsDetected: Boolean = false,
    val elevatorDetected: Boolean = false
)

/**
 * Light sensor service
 */
@Singleton
class LightSensorService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sensorManager: SensorManager? = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Exception) {
        null
    }
    private val lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val _lightState = MutableStateFlow(LightState())
    val lightState: StateFlow<LightState> = _lightState.asStateFlow()

    private var previousLight: Float = 0f

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val lux = event.values[0]

            val trend = when {
                lux > previousLight * 1.5f -> Trend.RISING
                lux < previousLight * 0.7f -> Trend.FALLING
                else -> Trend.STABLE
            }

            val isOutdoor = lux > 1000  // Bright = likely outdoor

            _lightState.value = LightState(
                lux = lux,
                trend = trend,
                isOutdoor = isOutdoor
            )

            previousLight = lux
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun startMeasuring() {
        try {
            lightSensor?.let {
                sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        } catch (e: Exception) {
            // Sensor not available
        }
    }

    fun stopMeasuring() {
        try {
            sensorManager?.unregisterListener(listener)
        } catch (e: Exception) {
            // Ignore
        }
    }
}

data class LightState(
    val lux: Float = 0f,
    val trend: Trend = Trend.STABLE,
    val isOutdoor: Boolean = false
)
