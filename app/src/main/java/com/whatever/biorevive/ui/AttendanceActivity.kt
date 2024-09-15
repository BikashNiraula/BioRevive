package com.whatever.biorevive.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatever.biorevive.database.attendance.AttendanceDatabaseManager
import com.whatever.biorevive.database.attendance.AttendanceDate
import com.whatever.biorevive.database.attendance.DateWithStudentAttendance
import com.whatever.biorevive.databinding.ActivityAttendanceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val attendanceDate = intent.getStringExtra("AttendanceDate")
        binding.rvAttendance.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            val studentAttendance = if(attendanceDate != null) {
                AttendanceDatabaseManager(applicationContext).attendanceDatabase.attendanceDao.getDateWithStudents(
                    attendanceDate
                )
            } else{
                DateWithStudentAttendance(AttendanceDate("", 0), listOf())
                //TODO("Resolve this null check error")
            }

            val customAdapter = AttendanceAdapter(
                studentAttendance.students.sortedBy { it.rollNo }.toMutableList(),
                this@AttendanceActivity)
            binding.rvAttendance.layoutManager = LinearLayoutManager(this@AttendanceActivity)
            binding.rvAttendance.adapter = customAdapter
            binding.tvPresentStudentsList.text = "Present Students Of: ${studentAttendance.attendanceDate.date.toString()}"
        }




    }
}