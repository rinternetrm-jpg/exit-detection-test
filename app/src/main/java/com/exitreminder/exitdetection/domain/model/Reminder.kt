package com.exitreminder.exitdetection.domain.model

/**
 * A WLAN-based exit reminder
 */
data class Reminder(
    val id: Long = 0,
    val name: String,  // "Zuhause", "Büro"
    val reminderText: String,  // "Hast du Schlüssel dabei?"
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastTriggeredAt: Long? = null,
    val triggerCount: Int = 0,
    val falseAlarmCount: Int = 0,

    // Location profile
    val profile: LocationProfile
) {
    val isAtLocation: Boolean
        get() = profile.wifiSsid.isNotEmpty()
}

/**
 * Current status of a reminder location
 */
data class ReminderStatus(
    val reminderId: Long,
    val isNearLocation: Boolean,
    val currentWifiSignal: Int?,
    val distanceFromLocation: Float?,
    val exitPrediction: ExitPrediction?
)

/**
 * Analysis result when creating a new reminder
 */
data class LocationAnalysis(
    val isComplete: Boolean = false,

    // Steps completed
    val gpsComplete: Boolean = false,
    val wifiComplete: Boolean = false,
    val mapLoaded: Boolean = false,
    val aiAnalysisComplete: Boolean = false,
    val profileComplete: Boolean = false,

    // Current step message
    val currentStep: String = "",

    // Results
    val profile: LocationProfile? = null,
    val error: String? = null
)
