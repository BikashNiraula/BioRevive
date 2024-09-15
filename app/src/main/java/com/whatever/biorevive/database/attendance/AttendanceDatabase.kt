package com.whatever.biorevive.database.attendance

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(
    entities = [AttendanceDate::class, StudentAttendance::class],
    version = 1
)
abstract class AttendanceDatabase:RoomDatabase() {

    abstract val attendanceDao: AttendanceDao
}