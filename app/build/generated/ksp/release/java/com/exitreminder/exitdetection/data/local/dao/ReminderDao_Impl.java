package com.exitreminder.exitdetection.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.exitreminder.exitdetection.data.local.entity.ExitEventEntity;
import com.exitreminder.exitdetection.data.local.entity.FalseAlarmEntity;
import com.exitreminder.exitdetection.data.local.entity.ReminderEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReminderDao_Impl implements ReminderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReminderEntity> __insertionAdapterOfReminderEntity;

  private final EntityInsertionAdapter<ExitEventEntity> __insertionAdapterOfExitEventEntity;

  private final EntityInsertionAdapter<FalseAlarmEntity> __insertionAdapterOfFalseAlarmEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderEntity> __deletionAdapterOfReminderEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderEntity> __updateAdapterOfReminderEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReminderById;

  private final SharedSQLiteStatement __preparedStmtOfMarkTriggered;

  private final SharedSQLiteStatement __preparedStmtOfIncrementFalseAlarm;

  private final SharedSQLiteStatement __preparedStmtOfSetActive;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEventsForReminder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldEvents;

  public ReminderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReminderEntity = new EntityInsertionAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reminders` (`id`,`name`,`reminderText`,`isActive`,`createdAt`,`lastTriggeredAt`,`triggerCount`,`falseAlarmCount`,`wifiSsid`,`wifiBssid`,`wifiSignalAtStart`,`latitude`,`longitude`,`gpsAccuracyAtStart`,`altitude`,`buildingType`,`estimatedFloor`,`totalFloors`,`hasGarden`,`gardenDirection`,`nearestStreetName`,`nearestStreetDistance`,`nearestStreetDirection`,`streetType`,`surroundingBuildings`,`isUrbanArea`,`baseAltitude`,`floorHeight`,`typicalExitDuration`,`possibleExitsJson`,`nearestPOIsJson`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getReminderText());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCreatedAt());
        if (entity.getLastTriggeredAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastTriggeredAt());
        }
        statement.bindLong(7, entity.getTriggerCount());
        statement.bindLong(8, entity.getFalseAlarmCount());
        statement.bindString(9, entity.getWifiSsid());
        statement.bindString(10, entity.getWifiBssid());
        statement.bindLong(11, entity.getWifiSignalAtStart());
        statement.bindDouble(12, entity.getLatitude());
        statement.bindDouble(13, entity.getLongitude());
        statement.bindDouble(14, entity.getGpsAccuracyAtStart());
        statement.bindDouble(15, entity.getAltitude());
        statement.bindString(16, entity.getBuildingType());
        statement.bindLong(17, entity.getEstimatedFloor());
        if (entity.getTotalFloors() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getTotalFloors());
        }
        final int _tmp_1 = entity.getHasGarden() ? 1 : 0;
        statement.bindLong(19, _tmp_1);
        if (entity.getGardenDirection() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getGardenDirection());
        }
        statement.bindString(21, entity.getNearestStreetName());
        statement.bindDouble(22, entity.getNearestStreetDistance());
        statement.bindString(23, entity.getNearestStreetDirection());
        statement.bindString(24, entity.getStreetType());
        statement.bindLong(25, entity.getSurroundingBuildings());
        final int _tmp_2 = entity.isUrbanArea() ? 1 : 0;
        statement.bindLong(26, _tmp_2);
        statement.bindDouble(27, entity.getBaseAltitude());
        statement.bindDouble(28, entity.getFloorHeight());
        if (entity.getTypicalExitDuration() == null) {
          statement.bindNull(29);
        } else {
          statement.bindLong(29, entity.getTypicalExitDuration());
        }
        statement.bindString(30, entity.getPossibleExitsJson());
        statement.bindString(31, entity.getNearestPOIsJson());
      }
    };
    this.__insertionAdapterOfExitEventEntity = new EntityInsertionAdapter<ExitEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exit_events` (`id`,`reminderId`,`timestamp`,`eventType`,`title`,`description`,`dataJson`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExitEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getReminderId());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getEventType());
        statement.bindString(5, entity.getTitle());
        statement.bindString(6, entity.getDescription());
        statement.bindString(7, entity.getDataJson());
      }
    };
    this.__insertionAdapterOfFalseAlarmEntity = new EntityInsertionAdapter<FalseAlarmEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `false_alarms` (`id`,`reminderId`,`timestamp`,`reason`,`sensorDataJson`,`predictionJson`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FalseAlarmEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getReminderId());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getReason());
        statement.bindString(5, entity.getSensorDataJson());
        statement.bindString(6, entity.getPredictionJson());
      }
    };
    this.__deletionAdapterOfReminderEntity = new EntityDeletionOrUpdateAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reminders` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReminderEntity = new EntityDeletionOrUpdateAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reminders` SET `id` = ?,`name` = ?,`reminderText` = ?,`isActive` = ?,`createdAt` = ?,`lastTriggeredAt` = ?,`triggerCount` = ?,`falseAlarmCount` = ?,`wifiSsid` = ?,`wifiBssid` = ?,`wifiSignalAtStart` = ?,`latitude` = ?,`longitude` = ?,`gpsAccuracyAtStart` = ?,`altitude` = ?,`buildingType` = ?,`estimatedFloor` = ?,`totalFloors` = ?,`hasGarden` = ?,`gardenDirection` = ?,`nearestStreetName` = ?,`nearestStreetDistance` = ?,`nearestStreetDirection` = ?,`streetType` = ?,`surroundingBuildings` = ?,`isUrbanArea` = ?,`baseAltitude` = ?,`floorHeight` = ?,`typicalExitDuration` = ?,`possibleExitsJson` = ?,`nearestPOIsJson` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getReminderText());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCreatedAt());
        if (entity.getLastTriggeredAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastTriggeredAt());
        }
        statement.bindLong(7, entity.getTriggerCount());
        statement.bindLong(8, entity.getFalseAlarmCount());
        statement.bindString(9, entity.getWifiSsid());
        statement.bindString(10, entity.getWifiBssid());
        statement.bindLong(11, entity.getWifiSignalAtStart());
        statement.bindDouble(12, entity.getLatitude());
        statement.bindDouble(13, entity.getLongitude());
        statement.bindDouble(14, entity.getGpsAccuracyAtStart());
        statement.bindDouble(15, entity.getAltitude());
        statement.bindString(16, entity.getBuildingType());
        statement.bindLong(17, entity.getEstimatedFloor());
        if (entity.getTotalFloors() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getTotalFloors());
        }
        final int _tmp_1 = entity.getHasGarden() ? 1 : 0;
        statement.bindLong(19, _tmp_1);
        if (entity.getGardenDirection() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getGardenDirection());
        }
        statement.bindString(21, entity.getNearestStreetName());
        statement.bindDouble(22, entity.getNearestStreetDistance());
        statement.bindString(23, entity.getNearestStreetDirection());
        statement.bindString(24, entity.getStreetType());
        statement.bindLong(25, entity.getSurroundingBuildings());
        final int _tmp_2 = entity.isUrbanArea() ? 1 : 0;
        statement.bindLong(26, _tmp_2);
        statement.bindDouble(27, entity.getBaseAltitude());
        statement.bindDouble(28, entity.getFloorHeight());
        if (entity.getTypicalExitDuration() == null) {
          statement.bindNull(29);
        } else {
          statement.bindLong(29, entity.getTypicalExitDuration());
        }
        statement.bindString(30, entity.getPossibleExitsJson());
        statement.bindString(31, entity.getNearestPOIsJson());
        statement.bindLong(32, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteReminderById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reminders WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkTriggered = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reminders SET lastTriggeredAt = ?, triggerCount = triggerCount + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementFalseAlarm = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reminders SET falseAlarmCount = falseAlarmCount + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetActive = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reminders SET isActive = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteEventsForReminder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exit_events WHERE reminderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldEvents = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exit_events WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertReminder(final ReminderEntity reminder,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReminderEntity.insertAndReturnId(reminder);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEvent(final ExitEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExitEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFalseAlarm(final FalseAlarmEntity falseAlarm,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFalseAlarmEntity.insertAndReturnId(falseAlarm);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReminder(final ReminderEntity reminder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfReminderEntity.handle(reminder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReminder(final ReminderEntity reminder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReminderEntity.handle(reminder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReminderById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteReminderById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteReminderById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markTriggered(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkTriggered.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkTriggered.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementFalseAlarm(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementFalseAlarm.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementFalseAlarm.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setActive(final long id, final boolean isActive,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetActive.acquire();
        int _argIndex = 1;
        final int _tmp = isActive ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetActive.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEventsForReminder(final long reminderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEventsForReminder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, reminderId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteEventsForReminder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldEvents(final long beforeTimestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldEvents.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, beforeTimestamp);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldEvents.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReminderEntity>> getAllReminders() {
    final String _sql = "SELECT * FROM reminders ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reminders"}, new Callable<List<ReminderEntity>>() {
      @Override
      @NonNull
      public List<ReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReminderText = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderText");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastTriggeredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggeredAt");
          final int _cursorIndexOfTriggerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerCount");
          final int _cursorIndexOfFalseAlarmCount = CursorUtil.getColumnIndexOrThrow(_cursor, "falseAlarmCount");
          final int _cursorIndexOfWifiSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSsid");
          final int _cursorIndexOfWifiBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiBssid");
          final int _cursorIndexOfWifiSignalAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSignalAtStart");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfGpsAccuracyAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAccuracyAtStart");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfBuildingType = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingType");
          final int _cursorIndexOfEstimatedFloor = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedFloor");
          final int _cursorIndexOfTotalFloors = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFloors");
          final int _cursorIndexOfHasGarden = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGarden");
          final int _cursorIndexOfGardenDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "gardenDirection");
          final int _cursorIndexOfNearestStreetName = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetName");
          final int _cursorIndexOfNearestStreetDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDistance");
          final int _cursorIndexOfNearestStreetDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDirection");
          final int _cursorIndexOfStreetType = CursorUtil.getColumnIndexOrThrow(_cursor, "streetType");
          final int _cursorIndexOfSurroundingBuildings = CursorUtil.getColumnIndexOrThrow(_cursor, "surroundingBuildings");
          final int _cursorIndexOfIsUrbanArea = CursorUtil.getColumnIndexOrThrow(_cursor, "isUrbanArea");
          final int _cursorIndexOfBaseAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAltitude");
          final int _cursorIndexOfFloorHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "floorHeight");
          final int _cursorIndexOfTypicalExitDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "typicalExitDuration");
          final int _cursorIndexOfPossibleExitsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "possibleExitsJson");
          final int _cursorIndexOfNearestPOIsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestPOIsJson");
          final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpReminderText;
            _tmpReminderText = _cursor.getString(_cursorIndexOfReminderText);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpLastTriggeredAt;
            if (_cursor.isNull(_cursorIndexOfLastTriggeredAt)) {
              _tmpLastTriggeredAt = null;
            } else {
              _tmpLastTriggeredAt = _cursor.getLong(_cursorIndexOfLastTriggeredAt);
            }
            final int _tmpTriggerCount;
            _tmpTriggerCount = _cursor.getInt(_cursorIndexOfTriggerCount);
            final int _tmpFalseAlarmCount;
            _tmpFalseAlarmCount = _cursor.getInt(_cursorIndexOfFalseAlarmCount);
            final String _tmpWifiSsid;
            _tmpWifiSsid = _cursor.getString(_cursorIndexOfWifiSsid);
            final String _tmpWifiBssid;
            _tmpWifiBssid = _cursor.getString(_cursorIndexOfWifiBssid);
            final int _tmpWifiSignalAtStart;
            _tmpWifiSignalAtStart = _cursor.getInt(_cursorIndexOfWifiSignalAtStart);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final float _tmpGpsAccuracyAtStart;
            _tmpGpsAccuracyAtStart = _cursor.getFloat(_cursorIndexOfGpsAccuracyAtStart);
            final double _tmpAltitude;
            _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            final String _tmpBuildingType;
            _tmpBuildingType = _cursor.getString(_cursorIndexOfBuildingType);
            final int _tmpEstimatedFloor;
            _tmpEstimatedFloor = _cursor.getInt(_cursorIndexOfEstimatedFloor);
            final Integer _tmpTotalFloors;
            if (_cursor.isNull(_cursorIndexOfTotalFloors)) {
              _tmpTotalFloors = null;
            } else {
              _tmpTotalFloors = _cursor.getInt(_cursorIndexOfTotalFloors);
            }
            final boolean _tmpHasGarden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasGarden);
            _tmpHasGarden = _tmp_1 != 0;
            final String _tmpGardenDirection;
            if (_cursor.isNull(_cursorIndexOfGardenDirection)) {
              _tmpGardenDirection = null;
            } else {
              _tmpGardenDirection = _cursor.getString(_cursorIndexOfGardenDirection);
            }
            final String _tmpNearestStreetName;
            _tmpNearestStreetName = _cursor.getString(_cursorIndexOfNearestStreetName);
            final float _tmpNearestStreetDistance;
            _tmpNearestStreetDistance = _cursor.getFloat(_cursorIndexOfNearestStreetDistance);
            final String _tmpNearestStreetDirection;
            _tmpNearestStreetDirection = _cursor.getString(_cursorIndexOfNearestStreetDirection);
            final String _tmpStreetType;
            _tmpStreetType = _cursor.getString(_cursorIndexOfStreetType);
            final int _tmpSurroundingBuildings;
            _tmpSurroundingBuildings = _cursor.getInt(_cursorIndexOfSurroundingBuildings);
            final boolean _tmpIsUrbanArea;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUrbanArea);
            _tmpIsUrbanArea = _tmp_2 != 0;
            final double _tmpBaseAltitude;
            _tmpBaseAltitude = _cursor.getDouble(_cursorIndexOfBaseAltitude);
            final float _tmpFloorHeight;
            _tmpFloorHeight = _cursor.getFloat(_cursorIndexOfFloorHeight);
            final Integer _tmpTypicalExitDuration;
            if (_cursor.isNull(_cursorIndexOfTypicalExitDuration)) {
              _tmpTypicalExitDuration = null;
            } else {
              _tmpTypicalExitDuration = _cursor.getInt(_cursorIndexOfTypicalExitDuration);
            }
            final String _tmpPossibleExitsJson;
            _tmpPossibleExitsJson = _cursor.getString(_cursorIndexOfPossibleExitsJson);
            final String _tmpNearestPOIsJson;
            _tmpNearestPOIsJson = _cursor.getString(_cursorIndexOfNearestPOIsJson);
            _item = new ReminderEntity(_tmpId,_tmpName,_tmpReminderText,_tmpIsActive,_tmpCreatedAt,_tmpLastTriggeredAt,_tmpTriggerCount,_tmpFalseAlarmCount,_tmpWifiSsid,_tmpWifiBssid,_tmpWifiSignalAtStart,_tmpLatitude,_tmpLongitude,_tmpGpsAccuracyAtStart,_tmpAltitude,_tmpBuildingType,_tmpEstimatedFloor,_tmpTotalFloors,_tmpHasGarden,_tmpGardenDirection,_tmpNearestStreetName,_tmpNearestStreetDistance,_tmpNearestStreetDirection,_tmpStreetType,_tmpSurroundingBuildings,_tmpIsUrbanArea,_tmpBaseAltitude,_tmpFloorHeight,_tmpTypicalExitDuration,_tmpPossibleExitsJson,_tmpNearestPOIsJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ReminderEntity>> getActiveReminders() {
    final String _sql = "SELECT * FROM reminders WHERE isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reminders"}, new Callable<List<ReminderEntity>>() {
      @Override
      @NonNull
      public List<ReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReminderText = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderText");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastTriggeredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggeredAt");
          final int _cursorIndexOfTriggerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerCount");
          final int _cursorIndexOfFalseAlarmCount = CursorUtil.getColumnIndexOrThrow(_cursor, "falseAlarmCount");
          final int _cursorIndexOfWifiSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSsid");
          final int _cursorIndexOfWifiBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiBssid");
          final int _cursorIndexOfWifiSignalAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSignalAtStart");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfGpsAccuracyAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAccuracyAtStart");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfBuildingType = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingType");
          final int _cursorIndexOfEstimatedFloor = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedFloor");
          final int _cursorIndexOfTotalFloors = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFloors");
          final int _cursorIndexOfHasGarden = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGarden");
          final int _cursorIndexOfGardenDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "gardenDirection");
          final int _cursorIndexOfNearestStreetName = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetName");
          final int _cursorIndexOfNearestStreetDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDistance");
          final int _cursorIndexOfNearestStreetDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDirection");
          final int _cursorIndexOfStreetType = CursorUtil.getColumnIndexOrThrow(_cursor, "streetType");
          final int _cursorIndexOfSurroundingBuildings = CursorUtil.getColumnIndexOrThrow(_cursor, "surroundingBuildings");
          final int _cursorIndexOfIsUrbanArea = CursorUtil.getColumnIndexOrThrow(_cursor, "isUrbanArea");
          final int _cursorIndexOfBaseAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAltitude");
          final int _cursorIndexOfFloorHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "floorHeight");
          final int _cursorIndexOfTypicalExitDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "typicalExitDuration");
          final int _cursorIndexOfPossibleExitsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "possibleExitsJson");
          final int _cursorIndexOfNearestPOIsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestPOIsJson");
          final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpReminderText;
            _tmpReminderText = _cursor.getString(_cursorIndexOfReminderText);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpLastTriggeredAt;
            if (_cursor.isNull(_cursorIndexOfLastTriggeredAt)) {
              _tmpLastTriggeredAt = null;
            } else {
              _tmpLastTriggeredAt = _cursor.getLong(_cursorIndexOfLastTriggeredAt);
            }
            final int _tmpTriggerCount;
            _tmpTriggerCount = _cursor.getInt(_cursorIndexOfTriggerCount);
            final int _tmpFalseAlarmCount;
            _tmpFalseAlarmCount = _cursor.getInt(_cursorIndexOfFalseAlarmCount);
            final String _tmpWifiSsid;
            _tmpWifiSsid = _cursor.getString(_cursorIndexOfWifiSsid);
            final String _tmpWifiBssid;
            _tmpWifiBssid = _cursor.getString(_cursorIndexOfWifiBssid);
            final int _tmpWifiSignalAtStart;
            _tmpWifiSignalAtStart = _cursor.getInt(_cursorIndexOfWifiSignalAtStart);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final float _tmpGpsAccuracyAtStart;
            _tmpGpsAccuracyAtStart = _cursor.getFloat(_cursorIndexOfGpsAccuracyAtStart);
            final double _tmpAltitude;
            _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            final String _tmpBuildingType;
            _tmpBuildingType = _cursor.getString(_cursorIndexOfBuildingType);
            final int _tmpEstimatedFloor;
            _tmpEstimatedFloor = _cursor.getInt(_cursorIndexOfEstimatedFloor);
            final Integer _tmpTotalFloors;
            if (_cursor.isNull(_cursorIndexOfTotalFloors)) {
              _tmpTotalFloors = null;
            } else {
              _tmpTotalFloors = _cursor.getInt(_cursorIndexOfTotalFloors);
            }
            final boolean _tmpHasGarden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasGarden);
            _tmpHasGarden = _tmp_1 != 0;
            final String _tmpGardenDirection;
            if (_cursor.isNull(_cursorIndexOfGardenDirection)) {
              _tmpGardenDirection = null;
            } else {
              _tmpGardenDirection = _cursor.getString(_cursorIndexOfGardenDirection);
            }
            final String _tmpNearestStreetName;
            _tmpNearestStreetName = _cursor.getString(_cursorIndexOfNearestStreetName);
            final float _tmpNearestStreetDistance;
            _tmpNearestStreetDistance = _cursor.getFloat(_cursorIndexOfNearestStreetDistance);
            final String _tmpNearestStreetDirection;
            _tmpNearestStreetDirection = _cursor.getString(_cursorIndexOfNearestStreetDirection);
            final String _tmpStreetType;
            _tmpStreetType = _cursor.getString(_cursorIndexOfStreetType);
            final int _tmpSurroundingBuildings;
            _tmpSurroundingBuildings = _cursor.getInt(_cursorIndexOfSurroundingBuildings);
            final boolean _tmpIsUrbanArea;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUrbanArea);
            _tmpIsUrbanArea = _tmp_2 != 0;
            final double _tmpBaseAltitude;
            _tmpBaseAltitude = _cursor.getDouble(_cursorIndexOfBaseAltitude);
            final float _tmpFloorHeight;
            _tmpFloorHeight = _cursor.getFloat(_cursorIndexOfFloorHeight);
            final Integer _tmpTypicalExitDuration;
            if (_cursor.isNull(_cursorIndexOfTypicalExitDuration)) {
              _tmpTypicalExitDuration = null;
            } else {
              _tmpTypicalExitDuration = _cursor.getInt(_cursorIndexOfTypicalExitDuration);
            }
            final String _tmpPossibleExitsJson;
            _tmpPossibleExitsJson = _cursor.getString(_cursorIndexOfPossibleExitsJson);
            final String _tmpNearestPOIsJson;
            _tmpNearestPOIsJson = _cursor.getString(_cursorIndexOfNearestPOIsJson);
            _item = new ReminderEntity(_tmpId,_tmpName,_tmpReminderText,_tmpIsActive,_tmpCreatedAt,_tmpLastTriggeredAt,_tmpTriggerCount,_tmpFalseAlarmCount,_tmpWifiSsid,_tmpWifiBssid,_tmpWifiSignalAtStart,_tmpLatitude,_tmpLongitude,_tmpGpsAccuracyAtStart,_tmpAltitude,_tmpBuildingType,_tmpEstimatedFloor,_tmpTotalFloors,_tmpHasGarden,_tmpGardenDirection,_tmpNearestStreetName,_tmpNearestStreetDistance,_tmpNearestStreetDirection,_tmpStreetType,_tmpSurroundingBuildings,_tmpIsUrbanArea,_tmpBaseAltitude,_tmpFloorHeight,_tmpTypicalExitDuration,_tmpPossibleExitsJson,_tmpNearestPOIsJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveRemindersNow(
      final Continuation<? super List<ReminderEntity>> $completion) {
    final String _sql = "SELECT * FROM reminders WHERE isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReminderEntity>>() {
      @Override
      @NonNull
      public List<ReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReminderText = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderText");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastTriggeredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggeredAt");
          final int _cursorIndexOfTriggerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerCount");
          final int _cursorIndexOfFalseAlarmCount = CursorUtil.getColumnIndexOrThrow(_cursor, "falseAlarmCount");
          final int _cursorIndexOfWifiSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSsid");
          final int _cursorIndexOfWifiBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiBssid");
          final int _cursorIndexOfWifiSignalAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSignalAtStart");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfGpsAccuracyAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAccuracyAtStart");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfBuildingType = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingType");
          final int _cursorIndexOfEstimatedFloor = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedFloor");
          final int _cursorIndexOfTotalFloors = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFloors");
          final int _cursorIndexOfHasGarden = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGarden");
          final int _cursorIndexOfGardenDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "gardenDirection");
          final int _cursorIndexOfNearestStreetName = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetName");
          final int _cursorIndexOfNearestStreetDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDistance");
          final int _cursorIndexOfNearestStreetDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDirection");
          final int _cursorIndexOfStreetType = CursorUtil.getColumnIndexOrThrow(_cursor, "streetType");
          final int _cursorIndexOfSurroundingBuildings = CursorUtil.getColumnIndexOrThrow(_cursor, "surroundingBuildings");
          final int _cursorIndexOfIsUrbanArea = CursorUtil.getColumnIndexOrThrow(_cursor, "isUrbanArea");
          final int _cursorIndexOfBaseAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAltitude");
          final int _cursorIndexOfFloorHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "floorHeight");
          final int _cursorIndexOfTypicalExitDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "typicalExitDuration");
          final int _cursorIndexOfPossibleExitsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "possibleExitsJson");
          final int _cursorIndexOfNearestPOIsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestPOIsJson");
          final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpReminderText;
            _tmpReminderText = _cursor.getString(_cursorIndexOfReminderText);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpLastTriggeredAt;
            if (_cursor.isNull(_cursorIndexOfLastTriggeredAt)) {
              _tmpLastTriggeredAt = null;
            } else {
              _tmpLastTriggeredAt = _cursor.getLong(_cursorIndexOfLastTriggeredAt);
            }
            final int _tmpTriggerCount;
            _tmpTriggerCount = _cursor.getInt(_cursorIndexOfTriggerCount);
            final int _tmpFalseAlarmCount;
            _tmpFalseAlarmCount = _cursor.getInt(_cursorIndexOfFalseAlarmCount);
            final String _tmpWifiSsid;
            _tmpWifiSsid = _cursor.getString(_cursorIndexOfWifiSsid);
            final String _tmpWifiBssid;
            _tmpWifiBssid = _cursor.getString(_cursorIndexOfWifiBssid);
            final int _tmpWifiSignalAtStart;
            _tmpWifiSignalAtStart = _cursor.getInt(_cursorIndexOfWifiSignalAtStart);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final float _tmpGpsAccuracyAtStart;
            _tmpGpsAccuracyAtStart = _cursor.getFloat(_cursorIndexOfGpsAccuracyAtStart);
            final double _tmpAltitude;
            _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            final String _tmpBuildingType;
            _tmpBuildingType = _cursor.getString(_cursorIndexOfBuildingType);
            final int _tmpEstimatedFloor;
            _tmpEstimatedFloor = _cursor.getInt(_cursorIndexOfEstimatedFloor);
            final Integer _tmpTotalFloors;
            if (_cursor.isNull(_cursorIndexOfTotalFloors)) {
              _tmpTotalFloors = null;
            } else {
              _tmpTotalFloors = _cursor.getInt(_cursorIndexOfTotalFloors);
            }
            final boolean _tmpHasGarden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasGarden);
            _tmpHasGarden = _tmp_1 != 0;
            final String _tmpGardenDirection;
            if (_cursor.isNull(_cursorIndexOfGardenDirection)) {
              _tmpGardenDirection = null;
            } else {
              _tmpGardenDirection = _cursor.getString(_cursorIndexOfGardenDirection);
            }
            final String _tmpNearestStreetName;
            _tmpNearestStreetName = _cursor.getString(_cursorIndexOfNearestStreetName);
            final float _tmpNearestStreetDistance;
            _tmpNearestStreetDistance = _cursor.getFloat(_cursorIndexOfNearestStreetDistance);
            final String _tmpNearestStreetDirection;
            _tmpNearestStreetDirection = _cursor.getString(_cursorIndexOfNearestStreetDirection);
            final String _tmpStreetType;
            _tmpStreetType = _cursor.getString(_cursorIndexOfStreetType);
            final int _tmpSurroundingBuildings;
            _tmpSurroundingBuildings = _cursor.getInt(_cursorIndexOfSurroundingBuildings);
            final boolean _tmpIsUrbanArea;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUrbanArea);
            _tmpIsUrbanArea = _tmp_2 != 0;
            final double _tmpBaseAltitude;
            _tmpBaseAltitude = _cursor.getDouble(_cursorIndexOfBaseAltitude);
            final float _tmpFloorHeight;
            _tmpFloorHeight = _cursor.getFloat(_cursorIndexOfFloorHeight);
            final Integer _tmpTypicalExitDuration;
            if (_cursor.isNull(_cursorIndexOfTypicalExitDuration)) {
              _tmpTypicalExitDuration = null;
            } else {
              _tmpTypicalExitDuration = _cursor.getInt(_cursorIndexOfTypicalExitDuration);
            }
            final String _tmpPossibleExitsJson;
            _tmpPossibleExitsJson = _cursor.getString(_cursorIndexOfPossibleExitsJson);
            final String _tmpNearestPOIsJson;
            _tmpNearestPOIsJson = _cursor.getString(_cursorIndexOfNearestPOIsJson);
            _item = new ReminderEntity(_tmpId,_tmpName,_tmpReminderText,_tmpIsActive,_tmpCreatedAt,_tmpLastTriggeredAt,_tmpTriggerCount,_tmpFalseAlarmCount,_tmpWifiSsid,_tmpWifiBssid,_tmpWifiSignalAtStart,_tmpLatitude,_tmpLongitude,_tmpGpsAccuracyAtStart,_tmpAltitude,_tmpBuildingType,_tmpEstimatedFloor,_tmpTotalFloors,_tmpHasGarden,_tmpGardenDirection,_tmpNearestStreetName,_tmpNearestStreetDistance,_tmpNearestStreetDirection,_tmpStreetType,_tmpSurroundingBuildings,_tmpIsUrbanArea,_tmpBaseAltitude,_tmpFloorHeight,_tmpTypicalExitDuration,_tmpPossibleExitsJson,_tmpNearestPOIsJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReminderById(final long id,
      final Continuation<? super ReminderEntity> $completion) {
    final String _sql = "SELECT * FROM reminders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReminderEntity>() {
      @Override
      @Nullable
      public ReminderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReminderText = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderText");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastTriggeredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggeredAt");
          final int _cursorIndexOfTriggerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerCount");
          final int _cursorIndexOfFalseAlarmCount = CursorUtil.getColumnIndexOrThrow(_cursor, "falseAlarmCount");
          final int _cursorIndexOfWifiSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSsid");
          final int _cursorIndexOfWifiBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiBssid");
          final int _cursorIndexOfWifiSignalAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSignalAtStart");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfGpsAccuracyAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAccuracyAtStart");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfBuildingType = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingType");
          final int _cursorIndexOfEstimatedFloor = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedFloor");
          final int _cursorIndexOfTotalFloors = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFloors");
          final int _cursorIndexOfHasGarden = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGarden");
          final int _cursorIndexOfGardenDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "gardenDirection");
          final int _cursorIndexOfNearestStreetName = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetName");
          final int _cursorIndexOfNearestStreetDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDistance");
          final int _cursorIndexOfNearestStreetDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDirection");
          final int _cursorIndexOfStreetType = CursorUtil.getColumnIndexOrThrow(_cursor, "streetType");
          final int _cursorIndexOfSurroundingBuildings = CursorUtil.getColumnIndexOrThrow(_cursor, "surroundingBuildings");
          final int _cursorIndexOfIsUrbanArea = CursorUtil.getColumnIndexOrThrow(_cursor, "isUrbanArea");
          final int _cursorIndexOfBaseAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAltitude");
          final int _cursorIndexOfFloorHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "floorHeight");
          final int _cursorIndexOfTypicalExitDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "typicalExitDuration");
          final int _cursorIndexOfPossibleExitsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "possibleExitsJson");
          final int _cursorIndexOfNearestPOIsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestPOIsJson");
          final ReminderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpReminderText;
            _tmpReminderText = _cursor.getString(_cursorIndexOfReminderText);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpLastTriggeredAt;
            if (_cursor.isNull(_cursorIndexOfLastTriggeredAt)) {
              _tmpLastTriggeredAt = null;
            } else {
              _tmpLastTriggeredAt = _cursor.getLong(_cursorIndexOfLastTriggeredAt);
            }
            final int _tmpTriggerCount;
            _tmpTriggerCount = _cursor.getInt(_cursorIndexOfTriggerCount);
            final int _tmpFalseAlarmCount;
            _tmpFalseAlarmCount = _cursor.getInt(_cursorIndexOfFalseAlarmCount);
            final String _tmpWifiSsid;
            _tmpWifiSsid = _cursor.getString(_cursorIndexOfWifiSsid);
            final String _tmpWifiBssid;
            _tmpWifiBssid = _cursor.getString(_cursorIndexOfWifiBssid);
            final int _tmpWifiSignalAtStart;
            _tmpWifiSignalAtStart = _cursor.getInt(_cursorIndexOfWifiSignalAtStart);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final float _tmpGpsAccuracyAtStart;
            _tmpGpsAccuracyAtStart = _cursor.getFloat(_cursorIndexOfGpsAccuracyAtStart);
            final double _tmpAltitude;
            _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            final String _tmpBuildingType;
            _tmpBuildingType = _cursor.getString(_cursorIndexOfBuildingType);
            final int _tmpEstimatedFloor;
            _tmpEstimatedFloor = _cursor.getInt(_cursorIndexOfEstimatedFloor);
            final Integer _tmpTotalFloors;
            if (_cursor.isNull(_cursorIndexOfTotalFloors)) {
              _tmpTotalFloors = null;
            } else {
              _tmpTotalFloors = _cursor.getInt(_cursorIndexOfTotalFloors);
            }
            final boolean _tmpHasGarden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasGarden);
            _tmpHasGarden = _tmp_1 != 0;
            final String _tmpGardenDirection;
            if (_cursor.isNull(_cursorIndexOfGardenDirection)) {
              _tmpGardenDirection = null;
            } else {
              _tmpGardenDirection = _cursor.getString(_cursorIndexOfGardenDirection);
            }
            final String _tmpNearestStreetName;
            _tmpNearestStreetName = _cursor.getString(_cursorIndexOfNearestStreetName);
            final float _tmpNearestStreetDistance;
            _tmpNearestStreetDistance = _cursor.getFloat(_cursorIndexOfNearestStreetDistance);
            final String _tmpNearestStreetDirection;
            _tmpNearestStreetDirection = _cursor.getString(_cursorIndexOfNearestStreetDirection);
            final String _tmpStreetType;
            _tmpStreetType = _cursor.getString(_cursorIndexOfStreetType);
            final int _tmpSurroundingBuildings;
            _tmpSurroundingBuildings = _cursor.getInt(_cursorIndexOfSurroundingBuildings);
            final boolean _tmpIsUrbanArea;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUrbanArea);
            _tmpIsUrbanArea = _tmp_2 != 0;
            final double _tmpBaseAltitude;
            _tmpBaseAltitude = _cursor.getDouble(_cursorIndexOfBaseAltitude);
            final float _tmpFloorHeight;
            _tmpFloorHeight = _cursor.getFloat(_cursorIndexOfFloorHeight);
            final Integer _tmpTypicalExitDuration;
            if (_cursor.isNull(_cursorIndexOfTypicalExitDuration)) {
              _tmpTypicalExitDuration = null;
            } else {
              _tmpTypicalExitDuration = _cursor.getInt(_cursorIndexOfTypicalExitDuration);
            }
            final String _tmpPossibleExitsJson;
            _tmpPossibleExitsJson = _cursor.getString(_cursorIndexOfPossibleExitsJson);
            final String _tmpNearestPOIsJson;
            _tmpNearestPOIsJson = _cursor.getString(_cursorIndexOfNearestPOIsJson);
            _result = new ReminderEntity(_tmpId,_tmpName,_tmpReminderText,_tmpIsActive,_tmpCreatedAt,_tmpLastTriggeredAt,_tmpTriggerCount,_tmpFalseAlarmCount,_tmpWifiSsid,_tmpWifiBssid,_tmpWifiSignalAtStart,_tmpLatitude,_tmpLongitude,_tmpGpsAccuracyAtStart,_tmpAltitude,_tmpBuildingType,_tmpEstimatedFloor,_tmpTotalFloors,_tmpHasGarden,_tmpGardenDirection,_tmpNearestStreetName,_tmpNearestStreetDistance,_tmpNearestStreetDirection,_tmpStreetType,_tmpSurroundingBuildings,_tmpIsUrbanArea,_tmpBaseAltitude,_tmpFloorHeight,_tmpTypicalExitDuration,_tmpPossibleExitsJson,_tmpNearestPOIsJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReminderBySsid(final String ssid,
      final Continuation<? super ReminderEntity> $completion) {
    final String _sql = "SELECT * FROM reminders WHERE wifiSsid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ssid);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReminderEntity>() {
      @Override
      @Nullable
      public ReminderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReminderText = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderText");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastTriggeredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggeredAt");
          final int _cursorIndexOfTriggerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerCount");
          final int _cursorIndexOfFalseAlarmCount = CursorUtil.getColumnIndexOrThrow(_cursor, "falseAlarmCount");
          final int _cursorIndexOfWifiSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSsid");
          final int _cursorIndexOfWifiBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiBssid");
          final int _cursorIndexOfWifiSignalAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiSignalAtStart");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfGpsAccuracyAtStart = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsAccuracyAtStart");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfBuildingType = CursorUtil.getColumnIndexOrThrow(_cursor, "buildingType");
          final int _cursorIndexOfEstimatedFloor = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedFloor");
          final int _cursorIndexOfTotalFloors = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFloors");
          final int _cursorIndexOfHasGarden = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGarden");
          final int _cursorIndexOfGardenDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "gardenDirection");
          final int _cursorIndexOfNearestStreetName = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetName");
          final int _cursorIndexOfNearestStreetDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDistance");
          final int _cursorIndexOfNearestStreetDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestStreetDirection");
          final int _cursorIndexOfStreetType = CursorUtil.getColumnIndexOrThrow(_cursor, "streetType");
          final int _cursorIndexOfSurroundingBuildings = CursorUtil.getColumnIndexOrThrow(_cursor, "surroundingBuildings");
          final int _cursorIndexOfIsUrbanArea = CursorUtil.getColumnIndexOrThrow(_cursor, "isUrbanArea");
          final int _cursorIndexOfBaseAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAltitude");
          final int _cursorIndexOfFloorHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "floorHeight");
          final int _cursorIndexOfTypicalExitDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "typicalExitDuration");
          final int _cursorIndexOfPossibleExitsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "possibleExitsJson");
          final int _cursorIndexOfNearestPOIsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "nearestPOIsJson");
          final ReminderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpReminderText;
            _tmpReminderText = _cursor.getString(_cursorIndexOfReminderText);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpLastTriggeredAt;
            if (_cursor.isNull(_cursorIndexOfLastTriggeredAt)) {
              _tmpLastTriggeredAt = null;
            } else {
              _tmpLastTriggeredAt = _cursor.getLong(_cursorIndexOfLastTriggeredAt);
            }
            final int _tmpTriggerCount;
            _tmpTriggerCount = _cursor.getInt(_cursorIndexOfTriggerCount);
            final int _tmpFalseAlarmCount;
            _tmpFalseAlarmCount = _cursor.getInt(_cursorIndexOfFalseAlarmCount);
            final String _tmpWifiSsid;
            _tmpWifiSsid = _cursor.getString(_cursorIndexOfWifiSsid);
            final String _tmpWifiBssid;
            _tmpWifiBssid = _cursor.getString(_cursorIndexOfWifiBssid);
            final int _tmpWifiSignalAtStart;
            _tmpWifiSignalAtStart = _cursor.getInt(_cursorIndexOfWifiSignalAtStart);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final float _tmpGpsAccuracyAtStart;
            _tmpGpsAccuracyAtStart = _cursor.getFloat(_cursorIndexOfGpsAccuracyAtStart);
            final double _tmpAltitude;
            _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            final String _tmpBuildingType;
            _tmpBuildingType = _cursor.getString(_cursorIndexOfBuildingType);
            final int _tmpEstimatedFloor;
            _tmpEstimatedFloor = _cursor.getInt(_cursorIndexOfEstimatedFloor);
            final Integer _tmpTotalFloors;
            if (_cursor.isNull(_cursorIndexOfTotalFloors)) {
              _tmpTotalFloors = null;
            } else {
              _tmpTotalFloors = _cursor.getInt(_cursorIndexOfTotalFloors);
            }
            final boolean _tmpHasGarden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasGarden);
            _tmpHasGarden = _tmp_1 != 0;
            final String _tmpGardenDirection;
            if (_cursor.isNull(_cursorIndexOfGardenDirection)) {
              _tmpGardenDirection = null;
            } else {
              _tmpGardenDirection = _cursor.getString(_cursorIndexOfGardenDirection);
            }
            final String _tmpNearestStreetName;
            _tmpNearestStreetName = _cursor.getString(_cursorIndexOfNearestStreetName);
            final float _tmpNearestStreetDistance;
            _tmpNearestStreetDistance = _cursor.getFloat(_cursorIndexOfNearestStreetDistance);
            final String _tmpNearestStreetDirection;
            _tmpNearestStreetDirection = _cursor.getString(_cursorIndexOfNearestStreetDirection);
            final String _tmpStreetType;
            _tmpStreetType = _cursor.getString(_cursorIndexOfStreetType);
            final int _tmpSurroundingBuildings;
            _tmpSurroundingBuildings = _cursor.getInt(_cursorIndexOfSurroundingBuildings);
            final boolean _tmpIsUrbanArea;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUrbanArea);
            _tmpIsUrbanArea = _tmp_2 != 0;
            final double _tmpBaseAltitude;
            _tmpBaseAltitude = _cursor.getDouble(_cursorIndexOfBaseAltitude);
            final float _tmpFloorHeight;
            _tmpFloorHeight = _cursor.getFloat(_cursorIndexOfFloorHeight);
            final Integer _tmpTypicalExitDuration;
            if (_cursor.isNull(_cursorIndexOfTypicalExitDuration)) {
              _tmpTypicalExitDuration = null;
            } else {
              _tmpTypicalExitDuration = _cursor.getInt(_cursorIndexOfTypicalExitDuration);
            }
            final String _tmpPossibleExitsJson;
            _tmpPossibleExitsJson = _cursor.getString(_cursorIndexOfPossibleExitsJson);
            final String _tmpNearestPOIsJson;
            _tmpNearestPOIsJson = _cursor.getString(_cursorIndexOfNearestPOIsJson);
            _result = new ReminderEntity(_tmpId,_tmpName,_tmpReminderText,_tmpIsActive,_tmpCreatedAt,_tmpLastTriggeredAt,_tmpTriggerCount,_tmpFalseAlarmCount,_tmpWifiSsid,_tmpWifiBssid,_tmpWifiSignalAtStart,_tmpLatitude,_tmpLongitude,_tmpGpsAccuracyAtStart,_tmpAltitude,_tmpBuildingType,_tmpEstimatedFloor,_tmpTotalFloors,_tmpHasGarden,_tmpGardenDirection,_tmpNearestStreetName,_tmpNearestStreetDistance,_tmpNearestStreetDirection,_tmpStreetType,_tmpSurroundingBuildings,_tmpIsUrbanArea,_tmpBaseAltitude,_tmpFloorHeight,_tmpTypicalExitDuration,_tmpPossibleExitsJson,_tmpNearestPOIsJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReminderCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM reminders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExitEventEntity>> getEventsForReminder(final long reminderId, final int limit) {
    final String _sql = "SELECT * FROM exit_events WHERE reminderId = ? ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reminderId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exit_events"}, new Callable<List<ExitEventEntity>>() {
      @Override
      @NonNull
      public List<ExitEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderId = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "dataJson");
          final List<ExitEventEntity> _result = new ArrayList<ExitEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExitEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpReminderId;
            _tmpReminderId = _cursor.getLong(_cursorIndexOfReminderId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpEventType;
            _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpDataJson;
            _tmpDataJson = _cursor.getString(_cursorIndexOfDataJson);
            _item = new ExitEventEntity(_tmpId,_tmpReminderId,_tmpTimestamp,_tmpEventType,_tmpTitle,_tmpDescription,_tmpDataJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ExitEventEntity>> getAllEvents(final int limit) {
    final String _sql = "SELECT * FROM exit_events ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exit_events"}, new Callable<List<ExitEventEntity>>() {
      @Override
      @NonNull
      public List<ExitEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderId = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "dataJson");
          final List<ExitEventEntity> _result = new ArrayList<ExitEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExitEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpReminderId;
            _tmpReminderId = _cursor.getLong(_cursorIndexOfReminderId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpEventType;
            _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpDataJson;
            _tmpDataJson = _cursor.getString(_cursorIndexOfDataJson);
            _item = new ExitEventEntity(_tmpId,_tmpReminderId,_tmpTimestamp,_tmpEventType,_tmpTitle,_tmpDescription,_tmpDataJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FalseAlarmEntity>> getFalseAlarmsForReminder(final long reminderId) {
    final String _sql = "SELECT * FROM false_alarms WHERE reminderId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reminderId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"false_alarms"}, new Callable<List<FalseAlarmEntity>>() {
      @Override
      @NonNull
      public List<FalseAlarmEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfReminderId = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfSensorDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorDataJson");
          final int _cursorIndexOfPredictionJson = CursorUtil.getColumnIndexOrThrow(_cursor, "predictionJson");
          final List<FalseAlarmEntity> _result = new ArrayList<FalseAlarmEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FalseAlarmEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpReminderId;
            _tmpReminderId = _cursor.getLong(_cursorIndexOfReminderId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final String _tmpSensorDataJson;
            _tmpSensorDataJson = _cursor.getString(_cursorIndexOfSensorDataJson);
            final String _tmpPredictionJson;
            _tmpPredictionJson = _cursor.getString(_cursorIndexOfPredictionJson);
            _item = new FalseAlarmEntity(_tmpId,_tmpReminderId,_tmpTimestamp,_tmpReason,_tmpSensorDataJson,_tmpPredictionJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object countFalseAlarmsByReason(final long reminderId, final String reason,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM false_alarms WHERE reminderId = ? AND reason = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reminderId);
    _argIndex = 2;
    _statement.bindString(_argIndex, reason);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
