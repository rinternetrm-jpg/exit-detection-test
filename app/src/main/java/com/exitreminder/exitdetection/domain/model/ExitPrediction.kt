package com.exitreminder.exitdetection.domain.model

/**
 * Exit prediction result with probability and factors
 */
data class ExitPrediction(
    val timestamp: Long = System.currentTimeMillis(),

    // === MAIN PREDICTION ===
    val exitProbability: Float,  // 0.0 - 1.0
    val exitType: PredictedExitType = PredictedExitType.UNKNOWN,
    val confidence: Float = 0f,

    // === DETAILS ===
    val estimatedSecondsToExit: Int? = null,
    val estimatedMetersToExit: Float? = null,
    val predictedExitPoint: ExitPoint? = null,

    // === FACTORS ===
    val factors: List<ExitFactor> = emptyList(),

    // === STATUS ===
    val status: ExitStatus = ExitStatus.HOME,
    val statusMessage: String = ""
) {
    val exitProbabilityPercent: Int
        get() = (exitProbability * 100).toInt()

    val shouldTrigger: Boolean
        get() = exitProbability >= 0.75f && status == ExitStatus.LEAVING
}

enum class PredictedExitType(val emoji: String, val displayName: String) {
    LEAVING_HOME("🚶", "Verlässt das Haus"),
    GOING_TO_GARDEN("🌳", "Geht in den Garten"),
    GOING_TO_GARAGE("🚗", "Geht zur Garage"),
    GOING_TO_BALCONY("🏠", "Geht auf Balkon"),
    TAKING_TRASH_OUT("🗑️", "Müll rausbringen"),
    FALSE_ALARM("❌", "Fehlalarm"),
    UNKNOWN("❓", "Unbekannt")
}

/**
 * Single factor contributing to exit prediction
 */
data class ExitFactor(
    val name: String,
    val weight: Float,  // 0-1 (how important is this factor)
    val value: Float,  // 0-1 (current contribution)
    val contributing: Boolean,  // true = speaks for exit
    val description: String
) {
    val contribution: Float
        get() = if (contributing) weight * value else -(weight * value)

    val percentContribution: Int
        get() = (contribution * 100).toInt()
}

enum class ExitStatus(val emoji: String, val displayName: String) {
    HOME("🏠", "Zuhause"),
    PROBABLY_HOME("🏠", "Wahrscheinlich zuhause"),
    UNCERTAIN("❓", "Unklar"),
    PROBABLY_LEAVING("🚶", "Wahrscheinlich am Gehen"),
    LEAVING("🚪", "Verlässt Gebäude"),
    OUTSIDE("🌳", "Draußen"),
    GARDEN("🌳", "Im Garten"),
    FALSE_ALARM("❌", "Fehlalarm")
}

/**
 * Event log entry for debugging
 */
data class ExitEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: ExitEventType,
    val title: String,
    val description: String = "",
    val data: Map<String, String> = emptyMap()
)

enum class ExitEventType(val emoji: String) {
    MONITORING_STARTED("🚀"),
    PROFILE_CREATED("✅"),
    WIFI_SIGNAL_CHANGE("📶"),
    MOVEMENT_DETECTED("🚶"),
    GPS_CHANGE("📍"),
    LIGHT_CHANGE("💡"),
    FLOOR_CHANGE("🛗"),
    EXIT_TRIGGERED("🔔"),
    EXIT_DETECTED("🚪"),
    REMINDER_DISMISSED("✅"),
    FALSE_ALARM("⚠️"),
    FALSE_ALARM_REPORTED("⚠️"),
    ERROR("❌")
}

/**
 * False alarm report for learning
 */
data class FalseAlarmReport(
    val timestamp: Long = System.currentTimeMillis(),
    val reminderId: Long,
    val reason: FalseAlarmReason,
    val sensorDataAtTrigger: LiveSensorData? = null,
    val predictionAtTrigger: ExitPrediction? = null,
    val userComment: String? = null
)

enum class FalseAlarmReason(val emoji: String, val displayName: String) {
    WAS_IN_GARDEN("🌳", "Ich war nur im Garten"),
    TOOK_TRASH_OUT("🗑️", "Ich habe nur den Müll rausgebracht"),
    CHECKED_MAILBOX("📬", "Ich habe nur den Briefkasten geleert"),
    WAS_IN_GARAGE("🚗", "Ich war nur in der Garage"),
    STAYED_INSIDE("🏠", "Ich war die ganze Zeit drinnen"),
    STILL_INSIDE("🏠", "Ich war noch drinnen"),
    OTHER("❓", "Sonstiges")
}
