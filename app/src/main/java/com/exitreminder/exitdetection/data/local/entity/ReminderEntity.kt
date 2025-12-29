package com.exitreminder.exitdetection.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val reminderText: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastTriggeredAt: Long? = null,
    val triggerCount: Int = 0,
    val falseAlarmCount: Int = 0,

    // Location Profile (embedded as JSON-like fields)
    val wifiSsid: String,
    val wifiBssid: String,
    val wifiSignalAtStart: Int,
    val latitude: Double,
    val longitude: Double,
    val gpsAccuracyAtStart: Float,
    val altitude: Double,

    // Building info
    val buildingType: String,
    val estimatedFloor: Int,
    val totalFloors: Int?,
    val hasGarden: Boolean,
    val gardenDirection: String?,

    // Street info
    val nearestStreetName: String,
    val nearestStreetDistance: Float,
    val nearestStreetDirection: String,
    val streetType: String,

    // Surroundings
    val surroundingBuildings: Int,
    val isUrbanArea: Boolean,

    // Height
    val baseAltitude: Double,
    val floorHeight: Float,
    val typicalExitDuration: Int?,

    // Serialized lists (JSON strings)
    val possibleExitsJson: String,  // List<ExitPoint> as JSON
    val nearestPOIsJson: String  // List<String> as JSON
)

@Entity(tableName = "exit_events")
data class ExitEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val eventType: String,
    val title: String,
    val description: String,
    val dataJson: String  // Map<String, String> as JSON
)

@Entity(tableName = "false_alarms")
data class FalseAlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val reason: String,
    val sensorDataJson: String,  // LiveSensorData as JSON
    val predictionJson: String  // ExitPrediction as JSON
)
