package io.github.jd1378.otphelper.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.jd1378.otphelper.data.local.dao.DetectedCodeDao
import io.github.jd1378.otphelper.data.local.dao.IgnoredNotifDao
import io.github.jd1378.otphelper.data.local.entity.DetectedCode
import io.github.jd1378.otphelper.data.local.entity.IgnoredNotif

/** The [RoomDatabase] we use in this app. */
@Database(
    entities =
        [
            IgnoredNotif::class,
            DetectedCode::class,
        ],
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    version = 2,
    exportSchema = true,
)
@TypeConverters(
    DateTimeTypeConverters::class,
)
abstract class OtpHelperDatabase : RoomDatabase() {
  abstract fun ignoredNotifDao(): IgnoredNotifDao

  abstract fun detectedCodeDao(): DetectedCodeDao
}
