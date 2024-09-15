package com.whatever.biorevive.database.attendance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_date")
data class AttendanceDate(
    @PrimaryKey(autoGenerate = false) val date: String,
    val numberOfStudentsPresent: Int
)