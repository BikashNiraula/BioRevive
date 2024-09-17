package com.whatever.biorevive.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatever.biorevive.database.attendance.AttendanceDatabaseManager
import com.whatever.biorevive.databinding.ActivityAttendanceDateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttendanceDateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceDateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceDateBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        //val date = mutableListOf(AttendanceDate("2020/02/12", 0))
        binding.rvAttendanceDate.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.Main).launch {
            val attendanceDateList=
                AttendanceDatabaseManager(applicationContext).attendanceDatabase.attendanceDao.getAllDatesWithStudents().toMutableList()
            var customAdapter = AttendanceDateAdapter(attendanceDateList, this@AttendanceDateActivity)

            binding.rvAttendanceDate.adapter = customAdapter
            customAdapter.OnItemClick = {
                val intent = Intent(this@AttendanceDateActivity, AttendanceActivity::class.java)
                intent.putExtra("AttendanceDate", it.attendanceDate.date)
                startActivity(intent)
            }
        }
    }
}