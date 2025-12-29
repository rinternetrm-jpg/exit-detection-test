package com.exitreminder.exitdetection.data.local.dao

import androidx.room.*
import com.exitreminder.exitdetection.data.local.entity.ExitEventEntity
import com.exitreminder.exitdetection.data.local.entity.FalseAlarmEntity
import com.exitreminder.exitdetection.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    // ===== REMINDERS =====

    @Query("SELECT * FROM reminders ORDER BY createdAt DESC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE isActive = 1")
    fun getActiveReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE isActive = 1")
    suspend fun getActiveRemindersNow(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): ReminderEntity?

    @Query("SELECT * FROM reminders WHERE wifiSsid = :ssid LIMIT 1")
    suspend fun getReminderBySsid(ssid: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    @Query("UPDATE reminders SET lastTriggeredAt = :timestamp, triggerCount = triggerCount + 1 WHERE id = :id")
    suspend fun markTriggered(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE reminders SET falseAlarmCount = falseAlarmCount + 1 WHERE id = :id")
    suspend fun incrementFalseAlarm(id: Long)

    @Query("UPDATE reminders SET isActive = :isActive WHERE id = :id")
    suspend fun setActive(id: Long, isActive: Boolean)

    @Query("SELECT COUNT(*) FROM reminders")
    suspend fun getReminderCount(): Int

    // ===== EXIT EVENTS =====

    @Query("SELECT * FROM exit_events WHERE reminderId = :reminderId ORDER BY timestamp DESC LIMIT :limit")
    fun getEventsForReminder(reminderId: Long, limit: Int = 100): Flow<List<ExitEventEntity>>

    @Query("SELECT * FROM exit_events ORDER BY timestamp DESC LIMIT :limit")
    fun getAllEvents(limit: Int = 500): Flow<List<ExitEventEntity>>

    @Insert
    suspend fun insertEvent(event: ExitEventEntity): Long

    @Query("DELETE FROM exit_events WHERE reminderId = :reminderId")
    suspend fun deleteEventsForReminder(reminderId: Long)

    @Query("DELETE FROM exit_events WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOldEvents(beforeTimestamp: Long)

    // ===== FALSE ALARMS =====

    @Query("SELECT * FROM false_alarms WHERE reminderId = :reminderId ORDER BY timestamp DESC")
    fun getFalseAlarmsForReminder(reminderId: Long): Flow<List<FalseAlarmEntity>>

    @Insert
    suspend fun insertFalseAlarm(falseAlarm: FalseAlarmEntity): Long

    @Query("SELECT COUNT(*) FROM false_alarms WHERE reminderId = :reminderId AND reason = :reason")
    suspend fun countFalseAlarmsByReason(reminderId: Long, reason: String): Int
}
