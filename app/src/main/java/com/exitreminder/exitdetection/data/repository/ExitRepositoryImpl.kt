package com.exitreminder.exitdetection.data.repository

import com.exitreminder.exitdetection.data.local.dao.ReminderDao
import com.exitreminder.exitdetection.data.local.entity.ExitEventEntity
import com.exitreminder.exitdetection.data.local.entity.FalseAlarmEntity
import com.exitreminder.exitdetection.data.local.entity.ReminderEntity
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExitRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val gson: Gson
) : ExitRepository {

    // === REMINDERS ===

    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.map { it.toReminder() }
        }
    }

    override fun getActiveReminders(): Flow<List<Reminder>> {
        return reminderDao.getActiveReminders().map { entities ->
            entities.map { it.toReminder() }
        }
    }

    override fun observeActiveReminders(): Flow<List<Reminder>> {
        return getActiveReminders()
    }

    override suspend fun getActiveRemindersNow(): List<Reminder> {
        return reminderDao.getActiveRemindersNow().map { it.toReminder() }
    }

    override suspend fun getReminderById(id: Long): Reminder? {
        return reminderDao.getReminderById(id)?.toReminder()
    }

    override suspend fun getReminderBySsid(ssid: String): Reminder? {
        return reminderDao.getReminderBySsid(ssid)?.toReminder()
    }

    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder.toEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toEntity())
    }

    override suspend fun deleteReminder(id: Long) {
        reminderDao.deleteReminderById(id)
        reminderDao.deleteEventsForReminder(id)
    }

    override suspend fun markTriggered(id: Long) {
        reminderDao.markTriggered(id)
    }

    override suspend fun incrementFalseAlarm(id: Long) {
        reminderDao.incrementFalseAlarm(id)
    }

    override suspend fun setActive(id: Long, isActive: Boolean) {
        reminderDao.setActive(id, isActive)
    }

    override suspend fun getReminderCount(): Int {
        return reminderDao.getReminderCount()
    }

    // === EVENTS ===

    override fun getEventsForReminder(reminderId: Long): Flow<List<ExitEvent>> {
        return reminderDao.getEventsForReminder(reminderId).map { entities ->
            entities.map { it.toExitEvent() }
        }
    }

    override fun getAllEvents(): Flow<List<ExitEvent>> {
        return reminderDao.getAllEvents().map { entities ->
            entities.map { it.toExitEvent() }
        }
    }

    override suspend fun logEvent(reminderId: Long, event: ExitEvent) {
        reminderDao.insertEvent(event.toEntity(reminderId))
    }

    override suspend fun deleteEventsForReminder(reminderId: Long) {
        reminderDao.deleteEventsForReminder(reminderId)
    }

    // === FALSE ALARMS ===

    override suspend fun reportFalseAlarm(report: FalseAlarmReport) {
        reminderDao.insertFalseAlarm(report.toEntity())
        reminderDao.incrementFalseAlarm(report.reminderId)
    }

    override suspend fun getFalseAlarmPatterns(reminderId: Long): Map<FalseAlarmReason, Int> {
        val patterns = mutableMapOf<FalseAlarmReason, Int>()
        FalseAlarmReason.entries.forEach { reason ->
            val count = reminderDao.countFalseAlarmsByReason(reminderId, reason.name)
            if (count > 0) {
                patterns[reason] = count
            }
        }
        return patterns
    }

    // === CONVERTERS ===

    private fun ReminderEntity.toReminder(): Reminder {
        val exitsType = object : TypeToken<List<ExitPoint>>() {}.type
        val poisType = object : TypeToken<List<String>>() {}.type

        val exits: List<ExitPoint> = try {
            gson.fromJson(possibleExitsJson, exitsType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val pois: List<String> = try {
            gson.fromJson(nearestPOIsJson, poisType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val profile = LocationProfile(
            id = id,
            createdAt = createdAt,
            wifiSsid = wifiSsid,
            wifiBssid = wifiBssid,
            wifiSignalAtStart = wifiSignalAtStart,
            latitude = latitude,
            longitude = longitude,
            gpsAccuracyAtStart = gpsAccuracyAtStart,
            altitude = altitude,
            buildingType = BuildingType.valueOf(buildingType),
            estimatedFloor = estimatedFloor,
            totalFloors = totalFloors,
            hasGarden = hasGarden,
            gardenDirection = gardenDirection?.let { Direction.valueOf(it) },
            nearestStreetName = nearestStreetName,
            nearestStreetDistance = nearestStreetDistance,
            nearestStreetDirection = Direction.valueOf(nearestStreetDirection),
            streetType = StreetType.valueOf(streetType),
            possibleExits = exits,
            surroundingBuildings = surroundingBuildings,
            isUrbanArea = isUrbanArea,
            nearestPOIs = pois,
            baseAltitude = baseAltitude,
            floorHeight = floorHeight,
            typicalExitDuration = typicalExitDuration
        )

        return Reminder(
            id = id,
            name = name,
            reminderText = reminderText,
            isActive = isActive,
            createdAt = createdAt,
            lastTriggeredAt = lastTriggeredAt,
            triggerCount = triggerCount,
            falseAlarmCount = falseAlarmCount,
            profile = profile
        )
    }

    private fun Reminder.toEntity(): ReminderEntity {
        return ReminderEntity(
            id = id,
            name = name,
            reminderText = reminderText,
            isActive = isActive,
            createdAt = createdAt,
            lastTriggeredAt = lastTriggeredAt,
            triggerCount = triggerCount,
            falseAlarmCount = falseAlarmCount,
            wifiSsid = profile.wifiSsid,
            wifiBssid = profile.wifiBssid,
            wifiSignalAtStart = profile.wifiSignalAtStart,
            latitude = profile.latitude,
            longitude = profile.longitude,
            gpsAccuracyAtStart = profile.gpsAccuracyAtStart,
            altitude = profile.altitude,
            buildingType = profile.buildingType.name,
            estimatedFloor = profile.estimatedFloor,
            totalFloors = profile.totalFloors,
            hasGarden = profile.hasGarden,
            gardenDirection = profile.gardenDirection?.name,
            nearestStreetName = profile.nearestStreetName,
            nearestStreetDistance = profile.nearestStreetDistance,
            nearestStreetDirection = profile.nearestStreetDirection.name,
            streetType = profile.streetType.name,
            surroundingBuildings = profile.surroundingBuildings,
            isUrbanArea = profile.isUrbanArea,
            baseAltitude = profile.baseAltitude,
            floorHeight = profile.floorHeight,
            typicalExitDuration = profile.typicalExitDuration,
            possibleExitsJson = gson.toJson(profile.possibleExits),
            nearestPOIsJson = gson.toJson(profile.nearestPOIs)
        )
    }

    private fun ExitEventEntity.toExitEvent(): ExitEvent {
        val dataType = object : TypeToken<Map<String, String>>() {}.type
        val data: Map<String, String> = try {
            gson.fromJson(dataJson, dataType) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }

        return ExitEvent(
            timestamp = timestamp,
            type = ExitEventType.valueOf(eventType),
            title = title,
            description = description,
            data = data
        )
    }

    private fun ExitEvent.toEntity(reminderId: Long): ExitEventEntity {
        return ExitEventEntity(
            reminderId = reminderId,
            timestamp = timestamp,
            eventType = type.name,
            title = title,
            description = description,
            dataJson = gson.toJson(data)
        )
    }

    private fun FalseAlarmReport.toEntity(): FalseAlarmEntity {
        return FalseAlarmEntity(
            reminderId = reminderId,
            timestamp = timestamp,
            reason = reason.name,
            sensorDataJson = gson.toJson(sensorDataAtTrigger),
            predictionJson = gson.toJson(predictionAtTrigger)
        )
    }
}
