package com.whatever.biorevive.database.attendance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AttendanceDao {

    // Insert a new attendance date
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttendanceDate(attendanceDate: AttendanceDate)

    // Insert a student's attendance data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentAttendance(studentAttendance: StudentAttendance)

    // Update the number of students present for a specific date
    @Query("UPDATE attendance_date SET numberOfStudentsPresent = :numberOfStudents WHERE date = :date")
    suspend fun updateNumberOfStudentsPresent(date: String, numberOfStudents: Int)

    // Count the number of students present for a specific date
    @Query("SELECT COUNT(*) FROM student_attendance WHERE dateOfAttendance = :date")
    suspend fun getNumberOfStudentsPresent(date: String): Int

    // Get all attendance dates with their respective students
    @Transaction
    @Query("SELECT * FROM attendance_date ORDER BY date DESC")
    suspend fun getAllDatesWithStudents(): List<DateWithStudentAttendance>

    // Get a specific date with its student attendance records
    @Transaction
    @Query("SELECT * FROM attendance_date WHERE date = :date")
    suspend fun getDateWithStudents(date: String): DateWithStudentAttendance
}
