package com.whatever.biorevive.database.attendance

import android.content.Context
import androidx.room.Room

class AttendanceDatabaseManager(context:Context) {
    val attendanceDatabase:AttendanceDatabase
    init {
        attendanceDatabase = Room.databaseBuilder(
            context,
            AttendanceDatabase::class.java,
            "my_attendance_database")
            .build()
    }
}