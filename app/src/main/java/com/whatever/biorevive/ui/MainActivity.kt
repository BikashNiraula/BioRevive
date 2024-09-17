package com.whatever.biorevive.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.whatever.biorevive.database.attendance.AttendanceDatabaseManager
import com.whatever.biorevive.database.attendance.AttendanceDate
import com.whatever.biorevive.databinding.ActivityMainBinding
import com.whatever.biorevive.utils.generateTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val todayDate = generateTodayDate()
            AttendanceDatabaseManager(applicationContext).attendanceDatabase.attendanceDao.insertAttendanceDate(
                AttendanceDate(todayDate, 0)
            )
        }


        binding.btnAttendance.setOnClickListener {
            val intent = Intent(this, RecognitionActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnAttendanceList.setOnClickListener{
            val intent = Intent(this, AttendanceDateActivity::class.java)
            startActivity(intent)
        }
    }


}