package com.whatever.biorevive.database.attendance

import androidx.room.Embedded
import androidx.room.Relation

data class DateWithStudentAttendance(
    @Embedded val attendanceDate: AttendanceDate,
    @Relation(
        parentColumn = "date",
        entityColumn = "dateOfAttendance"
    )
    val students: List<StudentAttendance>
)
