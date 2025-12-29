package com.exitreminder.exitdetection.service

import com.exitreminder.exitdetection.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

/**
 * Exit prediction algorithm
 * Analyzes sensor data to predict when user is leaving a location
 */
@Singleton
class ExitPredictor @Inject constructor() {

    /**
     * Calculate exit probability based on profile and current sensor data
     */
    fun calculateExitProbability(
        profile: LocationProfile,
        sensorData: LiveSensorData
    ): ExitPrediction {
        val factors = mutableListOf<ExitFactor>()
        var probability = 0f

        // ═══════════════════════════════════════════════════
        // FACTOR 1: WLAN Signal (max 30%)
        // ═══════════════════════════════════════════════════
        val wifiDrop = profile.wifiSignalAtStart - sensorData.wifiSignal
        val wifiFactor = when {
            wifiDrop > 30 -> 0.30f
            wifiDrop > 20 -> 0.25f
            wifiDrop > 10 -> 0.15f
            wifiDrop > 5 -> 0.05f
            else -> 0f
        }
        if (wifiFactor > 0) {
            factors.add(ExitFactor(
                name = "WLAN Signal fällt",
                weight = 0.30f,
                value = wifiFactor / 0.30f,
                contributing = true,
                description = "${profile.wifiSignalAtStart} → ${sensorData.wifiSignal} dBm (Δ $wifiDrop)"
            ))
            probability += wifiFactor
        }

        // WLAN completely lost = very strong indicator
        if (!sensorData.wifiConnected) {
            factors.add(ExitFactor(
                name = "WLAN Verbindung verloren",
                weight = 0.25f,
                value = 1f,
                contributing = true,
                description = "Keine Verbindung mehr zu ${profile.wifiSsid}"
            ))
            probability += 0.25f
        }

        // ═══════════════════════════════════════════════════
        // FACTOR 2: Movement + Direction (max 25%)
        // ═══════════════════════════════════════════════════
        if (sensorData.isWalking || sensorData.stepsSinceLastCheck > 5) {
            var movementFactor = 0.10f

            // Bonus if moving towards street
            if (sensorData.movingTowardsStreet) {
                movementFactor += 0.10f
                factors.add(ExitFactor(
                    name = "Bewegt sich zur Straße",
                    weight = 0.15f,
                    value = 1f,
                    contributing = true,
                    description = "Richtung: ${sensorData.currentDirection.symbol} ${sensorData.currentDirection.name}"
                ))
            }

            // Bonus if NOT moving towards garden
            if (profile.hasGarden && profile.gardenDirection != null) {
                if (sensorData.currentDirection != profile.gardenDirection) {
                    movementFactor += 0.05f
                }
            }

            factors.add(ExitFactor(
                name = "User geht",
                weight = 0.10f,
                value = 1f,
                contributing = true,
                description = "${sensorData.stepsSinceLastCheck} Schritte in 10 Sek"
            ))

            probability += movementFactor
        }

        // ═══════════════════════════════════════════════════
        // FACTOR 3: Distance to Street (max 20%)
        // ═══════════════════════════════════════════════════
        val streetProximityFactor = when {
            sensorData.distanceToStreet < 5 -> 0.20f
            sensorData.distanceToStreet < 10 -> 0.15f
            sensorData.distanceToStreet < 15 -> 0.10f
            sensorData.distanceToStreet < 20 -> 0.05f
            else -> 0f
        }
        if (streetProximityFactor > 0) {
            factors.add(ExitFactor(
                name = "Nah an Straße",
                weight = 0.20f,
                value = streetProximityFactor / 0.20f,
                contributing = true,
                description = "${sensorData.distanceToStreet.toInt()}m bis ${profile.nearestStreetName}"
            ))
            probability += streetProximityFactor
        }

        // ═══════════════════════════════════════════════════
        // FACTOR 4: GPS Accuracy (max 15%)
        // Indoor = bad GPS, Outdoor = good GPS
        // ═══════════════════════════════════════════════════
        val gpsImproved = profile.gpsAccuracyAtStart - sensorData.gpsAccuracy
        if (gpsImproved > 10 && sensorData.gpsAccuracy < 15) {
            factors.add(ExitFactor(
                name = "GPS verbessert (outdoor?)",
                weight = 0.15f,
                value = 0.8f,
                contributing = true,
                description = "${profile.gpsAccuracyAtStart.toInt()}m → ${sensorData.gpsAccuracy.toInt()}m"
            ))
            probability += 0.12f
        }

        // ═══════════════════════════════════════════════════
        // FACTOR 5: Light (max 10%)
        // ═══════════════════════════════════════════════════
        if (sensorData.lightLevel > 1000 && sensorData.lightTrend == Trend.RISING) {
            factors.add(ExitFactor(
                name = "Licht steigt (outdoor?)",
                weight = 0.10f,
                value = 0.7f,
                contributing = true,
                description = "${sensorData.lightLevel.toInt()} Lux"
            ))
            probability += 0.07f
        }

        // ═══════════════════════════════════════════════════
        // FACTOR 6: Floor / Altitude (for high-rise buildings)
        // ═══════════════════════════════════════════════════
        if (profile.buildingType in listOf(BuildingType.HIGHRISE, BuildingType.OFFICE_COMPLEX, BuildingType.OFFICE)) {
            // Arrived at ground floor
            if (sensorData.estimatedFloor == 0 && profile.estimatedFloor > 0) {
                factors.add(ExitFactor(
                    name = "Im Erdgeschoss angekommen",
                    weight = 0.15f,
                    value = 1f,
                    contributing = true,
                    description = "Etage ${profile.estimatedFloor} → EG"
                ))
                probability += 0.15f
            }

            // Elevator or stairs detected
            if (sensorData.elevatorDetected) {
                factors.add(ExitFactor(
                    name = "Fahrstuhl erkannt",
                    weight = 0.10f,
                    value = 0.6f,
                    contributing = true,
                    description = "Schnelle Höhenänderung erkannt"
                ))
                probability += 0.06f
            } else if (sensorData.stairsDetected) {
                factors.add(ExitFactor(
                    name = "Treppe erkannt",
                    weight = 0.10f,
                    value = 0.5f,
                    contributing = true,
                    description = "Langsame Höhenänderung erkannt"
                ))
                probability += 0.05f
            }
        }

        // ═══════════════════════════════════════════════════
        // NEGATIVE FACTORS
        // ═══════════════════════════════════════════════════

        // Garden detection (reduces probability)
        if (profile.hasGarden && profile.gardenDirection != null) {
            if (sensorData.currentDirection == profile.gardenDirection && sensorData.wifiSignal > -80) {
                factors.add(ExitFactor(
                    name = "Möglicherweise Garten",
                    weight = 0.15f,
                    value = 0.5f,
                    contributing = false,
                    description = "Richtung zum Garten (${profile.gardenDirection.symbol}), WLAN noch ok"
                ))
                probability -= 0.08f
            }
        }

        // Still connected to WiFi with good signal
        if (sensorData.wifiConnected && sensorData.wifiSignalPercent > 60) {
            factors.add(ExitFactor(
                name = "Noch mit WLAN verbunden",
                weight = 0.05f,
                value = 0.5f,
                contributing = false,
                description = "Signal bei ${sensorData.wifiSignalPercent}%"
            ))
            probability -= 0.02f
        }

        // ═══════════════════════════════════════════════════
        // FINAL CALCULATION
        // ═══════════════════════════════════════════════════
        probability = probability.coerceIn(0f, 1f)

        val status = when {
            probability < 0.20f -> ExitStatus.HOME
            probability < 0.40f -> ExitStatus.PROBABLY_HOME
            probability < 0.60f -> ExitStatus.UNCERTAIN
            probability < 0.80f -> ExitStatus.PROBABLY_LEAVING
            else -> ExitStatus.LEAVING
        }

        val exitType = determineExitType(sensorData, profile)
        val confidence = calculateConfidence(factors)
        val estimatedTime = estimateTimeToExit(sensorData, profile)

        return ExitPrediction(
            exitProbability = probability,
            exitType = exitType,
            confidence = confidence,
            estimatedSecondsToExit = estimatedTime,
            estimatedMetersToExit = sensorData.distanceToStreet,
            predictedExitPoint = profile.possibleExits.firstOrNull {
                it.direction == sensorData.currentDirection
            },
            factors = factors,
            status = status,
            statusMessage = getStatusMessage(status, probability)
        )
    }

    private fun determineExitType(
        sensorData: LiveSensorData,
        profile: LocationProfile
    ): PredictedExitType {
        // Check if moving towards garden
        if (profile.hasGarden && profile.gardenDirection != null) {
            if (sensorData.currentDirection == profile.gardenDirection &&
                sensorData.wifiSignal > -75) {
                return PredictedExitType.GOING_TO_GARDEN
            }
        }

        // Check if going to garage
        profile.possibleExits.find { it.exitType == ExitType.GARAGE }?.let { garageExit ->
            if (sensorData.currentDirection == garageExit.direction) {
                return PredictedExitType.GOING_TO_GARAGE
            }
        }

        // Default: leaving home
        return if (sensorData.movingTowardsStreet) {
            PredictedExitType.LEAVING_HOME
        } else {
            PredictedExitType.UNKNOWN
        }
    }

    private fun calculateConfidence(factors: List<ExitFactor>): Float {
        if (factors.isEmpty()) return 0f

        // Higher confidence if multiple factors agree
        val positiveFactors = factors.count { it.contributing }
        val negativeFactors = factors.count { !it.contributing }

        val agreement = positiveFactors.toFloat() / (positiveFactors + negativeFactors).toFloat()
        val totalWeight = factors.sumOf { it.weight.toDouble() }.toFloat()

        return (agreement * 0.6f + (totalWeight / 1.5f) * 0.4f).coerceIn(0f, 1f)
    }

    private fun estimateTimeToExit(
        sensorData: LiveSensorData,
        profile: LocationProfile
    ): Int? {
        if (!sensorData.isWalking) return null

        val speed = if (sensorData.gpsSpeed > 0.5f) {
            sensorData.gpsSpeed
        } else {
            1.0f  // Average walking speed
        }

        val distanceToExit = sensorData.distanceToStreet
        return (distanceToExit / speed).toInt()
    }

    private fun getStatusMessage(status: ExitStatus, probability: Float): String {
        val percent = (probability * 100).toInt()
        return when (status) {
            ExitStatus.HOME -> "Du bist zuhause ($percent%)"
            ExitStatus.PROBABLY_HOME -> "Wahrscheinlich zuhause ($percent%)"
            ExitStatus.UNCERTAIN -> "Unklar ($percent%)"
            ExitStatus.PROBABLY_LEAVING -> "Wahrscheinlich am Gehen ($percent%)"
            ExitStatus.LEAVING -> "Verlässt Gebäude ($percent%)"
            ExitStatus.OUTSIDE -> "Draußen ($percent%)"
            ExitStatus.GARDEN -> "Im Garten ($percent%)"
            ExitStatus.FALSE_ALARM -> "Fehlalarm"
        }
    }

    /**
     * Calculate distance between two GPS coordinates in meters
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val earthRadius = 6371000.0  // meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }

    /**
     * Calculate bearing between two GPS coordinates
     */
    fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)

        val x = sin(dLon) * cos(lat2Rad)
        val y = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(dLon)

        val bearing = Math.toDegrees(atan2(x, y))
        return ((bearing + 360) % 360).toFloat()
    }
}
