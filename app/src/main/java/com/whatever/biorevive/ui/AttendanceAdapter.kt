package com.whatever.biorevive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whatever.biorevive.database.attendance.StudentAttendance
import com.whatever.biorevive.databinding.CustomAttendanceBoxBinding

class AttendanceAdapter(
    private val studentAttendance: MutableList<StudentAttendance>,
    val context: Context
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: CustomAttendanceBoxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomAttendanceBoxBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = studentAttendance.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = studentAttendance[position].name
        holder.binding.tvRollNo.text = studentAttendance[position].rollNo
        holder.binding.tvSerialNo.text = (position + 1).toString() // Serial number typically starts from 1


    }
}
