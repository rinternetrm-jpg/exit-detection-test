package com.exitreminder.exitdetection.domain.model

/**
 * Real-time sensor data for exit detection
 * Updated every few seconds while monitoring
 */
data class LiveSensorData(
    val timestamp: Long = System.currentTimeMillis(),

    // === WLAN ===
    val wifiConnected: Boolean = false,
    val wifiSsid: String? = null,
    val wifiSignal: Int = -100,  // dBm
    val wifiSignalPercent: Int = 0,  // 0-100%
    val wifiSignalTrend: Trend = Trend.STABLE,

    // === GPS ===
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val gpsAccuracy: Float = 100f,  // meters
    val gpsSpeed: Float = 0f,  // m/s
    val gpsBearing: Float = 0f,  // 0-360°

    // === MOVEMENT ===
    val steps: Int = 0,
    val stepsSinceLastCheck: Int = 0,
    val isWalking: Boolean = false,
    val isRunning: Boolean = false,
    val walkingDirection: Direction? = null,

    // === ALTITUDE ===
    val altitude: Double = 0.0,  // meters above sea level
    val altitudeChange: Float = 0f,  // change since start
    val estimatedFloor: Int = 0,
    val floorChange: FloorChange = FloorChange.SAME,
    val stairsDetected: Boolean = false,
    val elevatorDetected: Boolean = false,

    // === LIGHT ===
    val lightLevel: Float = 0f,  // Lux
    val lightTrend: Trend = Trend.STABLE,

    // === CALCULATED DISTANCES ===
    val distanceFromStart: Float = 0f,  // meters from starting point
    val distanceToStreet: Float = 0f,  // meters to nearest street
    val distanceToNearestExit: Float = 0f,

    // === DIRECTION ===
    val movingTowardsStreet: Boolean = false,
    val movingTowardsExit: Boolean = false,
    val currentDirection: Direction = Direction.NORTH
) {
    companion object {
        fun dbmToPercent(dbm: Int): Int {
            return when {
                dbm >= -50 -> 100
                dbm <= -100 -> 0
                else -> 2 * (dbm + 100)
            }
        }
    }
}

enum class Trend(val symbol: String) {
    RISING("📈"),
    FALLING("📉"),
    STABLE("─")
}

enum class FloorChange(val symbol: String) {
    UP("⬆️"),
    DOWN("⬇️"),
    SAME("─")
}
