package com.exitreminder.exitdetection.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.exitreminder.exitdetection.data.local.dao.ReminderDao;
import com.exitreminder.exitdetection.data.local.dao.ReminderDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ReminderDao _reminderDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `reminderText` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `lastTriggeredAt` INTEGER, `triggerCount` INTEGER NOT NULL, `falseAlarmCount` INTEGER NOT NULL, `wifiSsid` TEXT NOT NULL, `wifiBssid` TEXT NOT NULL, `wifiSignalAtStart` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `gpsAccuracyAtStart` REAL NOT NULL, `altitude` REAL NOT NULL, `buildingType` TEXT NOT NULL, `estimatedFloor` INTEGER NOT NULL, `totalFloors` INTEGER, `hasGarden` INTEGER NOT NULL, `gardenDirection` TEXT, `nearestStreetName` TEXT NOT NULL, `nearestStreetDistance` REAL NOT NULL, `nearestStreetDirection` TEXT NOT NULL, `streetType` TEXT NOT NULL, `surroundingBuildings` INTEGER NOT NULL, `isUrbanArea` INTEGER NOT NULL, `baseAltitude` REAL NOT NULL, `floorHeight` REAL NOT NULL, `typicalExitDuration` INTEGER, `possibleExitsJson` TEXT NOT NULL, `nearestPOIsJson` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exit_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reminderId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `eventType` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `dataJson` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `false_alarms` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reminderId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `reason` TEXT NOT NULL, `sensorDataJson` TEXT NOT NULL, `predictionJson` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '26fe13c11d174cc430bc2171fa34428d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `reminders`");
        db.execSQL("DROP TABLE IF EXISTS `exit_events`");
        db.execSQL("DROP TABLE IF EXISTS `false_alarms`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsReminders = new HashMap<String, TableInfo.Column>(31);
        _columnsReminders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("reminderText", new TableInfo.Column("reminderText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("lastTriggeredAt", new TableInfo.Column("lastTriggeredAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("triggerCount", new TableInfo.Column("triggerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("falseAlarmCount", new TableInfo.Column("falseAlarmCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("wifiSsid", new TableInfo.Column("wifiSsid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("wifiBssid", new TableInfo.Column("wifiBssid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("wifiSignalAtStart", new TableInfo.Column("wifiSignalAtStart", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("gpsAccuracyAtStart", new TableInfo.Column("gpsAccuracyAtStart", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("altitude", new TableInfo.Column("altitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("buildingType", new TableInfo.Column("buildingType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("estimatedFloor", new TableInfo.Column("estimatedFloor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("totalFloors", new TableInfo.Column("totalFloors", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("hasGarden", new TableInfo.Column("hasGarden", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("gardenDirection", new TableInfo.Column("gardenDirection", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("nearestStreetName", new TableInfo.Column("nearestStreetName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("nearestStreetDistance", new TableInfo.Column("nearestStreetDistance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("nearestStreetDirection", new TableInfo.Column("nearestStreetDirection", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("streetType", new TableInfo.Column("streetType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("surroundingBuildings", new TableInfo.Column("surroundingBuildings", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("isUrbanArea", new TableInfo.Column("isUrbanArea", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("baseAltitude", new TableInfo.Column("baseAltitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("floorHeight", new TableInfo.Column("floorHeight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("typicalExitDuration", new TableInfo.Column("typicalExitDuration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("possibleExitsJson", new TableInfo.Column("possibleExitsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("nearestPOIsJson", new TableInfo.Column("nearestPOIsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReminders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReminders = new TableInfo("reminders", _columnsReminders, _foreignKeysReminders, _indicesReminders);
        final TableInfo _existingReminders = TableInfo.read(db, "reminders");
        if (!_infoReminders.equals(_existingReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "reminders(com.exitreminder.exitdetection.data.local.entity.ReminderEntity).\n"
                  + " Expected:\n" + _infoReminders + "\n"
                  + " Found:\n" + _existingReminders);
        }
        final HashMap<String, TableInfo.Column> _columnsExitEvents = new HashMap<String, TableInfo.Column>(7);
        _columnsExitEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("reminderId", new TableInfo.Column("reminderId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExitEvents.put("dataJson", new TableInfo.Column("dataJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExitEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExitEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExitEvents = new TableInfo("exit_events", _columnsExitEvents, _foreignKeysExitEvents, _indicesExitEvents);
        final TableInfo _existingExitEvents = TableInfo.read(db, "exit_events");
        if (!_infoExitEvents.equals(_existingExitEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "exit_events(com.exitreminder.exitdetection.data.local.entity.ExitEventEntity).\n"
                  + " Expected:\n" + _infoExitEvents + "\n"
                  + " Found:\n" + _existingExitEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsFalseAlarms = new HashMap<String, TableInfo.Column>(6);
        _columnsFalseAlarms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFalseAlarms.put("reminderId", new TableInfo.Column("reminderId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFalseAlarms.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFalseAlarms.put("reason", new TableInfo.Column("reason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFalseAlarms.put("sensorDataJson", new TableInfo.Column("sensorDataJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFalseAlarms.put("predictionJson", new TableInfo.Column("predictionJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFalseAlarms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFalseAlarms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFalseAlarms = new TableInfo("false_alarms", _columnsFalseAlarms, _foreignKeysFalseAlarms, _indicesFalseAlarms);
        final TableInfo _existingFalseAlarms = TableInfo.read(db, "false_alarms");
        if (!_infoFalseAlarms.equals(_existingFalseAlarms)) {
          return new RoomOpenHelper.ValidationResult(false, "false_alarms(com.exitreminder.exitdetection.data.local.entity.FalseAlarmEntity).\n"
                  + " Expected:\n" + _infoFalseAlarms + "\n"
                  + " Found:\n" + _existingFalseAlarms);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "26fe13c11d174cc430bc2171fa34428d", "b1a7dfad375f1b4b7c74060da1f28e72");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "reminders","exit_events","false_alarms");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `reminders`");
      _db.execSQL("DELETE FROM `exit_events`");
      _db.execSQL("DELETE FROM `false_alarms`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ReminderDao.class, ReminderDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ReminderDao reminderDao() {
    if (_reminderDao != null) {
      return _reminderDao;
    } else {
      synchronized(this) {
        if(_reminderDao == null) {
          _reminderDao = new ReminderDao_Impl(this);
        }
        return _reminderDao;
      }
    }
  }
}
