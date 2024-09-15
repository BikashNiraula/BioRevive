package com.whatever.biorevive.database.attendance

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "student_attendance",
    foreignKeys = [
        ForeignKey(
            entity = AttendanceDate::class,
            parentColumns = ["date"],
            childColumns = ["dateOfAttendance"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dateOfAttendance"])],
    primaryKeys = ["rollNo", "dateOfAttendance"] // Composite primary key


)
data class StudentAttendance(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val rollNo: String,
    val dateOfAttendance: String // Foreign key referencing AttendanceDate's date
)
