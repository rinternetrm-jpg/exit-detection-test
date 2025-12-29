package com.exitreminder.exitdetection.domain.repository

import com.exitreminder.exitdetection.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ExitRepository {

    // === REMINDERS ===
    fun getAllReminders(): Flow<List<Reminder>>
    fun getActiveReminders(): Flow<List<Reminder>>
    fun observeActiveReminders(): Flow<List<Reminder>>
    suspend fun getActiveRemindersNow(): List<Reminder>
    suspend fun getReminderById(id: Long): Reminder?
    suspend fun getReminderBySsid(ssid: String): Reminder?
    suspend fun insertReminder(reminder: Reminder): Long
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(id: Long)
    suspend fun markTriggered(id: Long)
    suspend fun incrementFalseAlarm(id: Long)
    suspend fun setActive(id: Long, isActive: Boolean)
    suspend fun getReminderCount(): Int

    // === EVENTS ===
    fun getEventsForReminder(reminderId: Long): Flow<List<ExitEvent>>
    fun getAllEvents(): Flow<List<ExitEvent>>
    suspend fun logEvent(reminderId: Long, event: ExitEvent)
    suspend fun deleteEventsForReminder(reminderId: Long)

    // === FALSE ALARMS ===
    suspend fun reportFalseAlarm(report: FalseAlarmReport)
    suspend fun getFalseAlarmPatterns(reminderId: Long): Map<FalseAlarmReason, Int>
}
