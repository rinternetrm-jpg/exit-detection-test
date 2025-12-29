package com.exitreminder.exitdetection.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.exitreminder.exitdetection.data.local.dao.ReminderDao
import com.exitreminder.exitdetection.data.local.entity.ExitEventEntity
import com.exitreminder.exitdetection.data.local.entity.FalseAlarmEntity
import com.exitreminder.exitdetection.data.local.entity.ReminderEntity

@Database(
    entities = [
        ReminderEntity::class,
        ExitEventEntity::class,
        FalseAlarmEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
