package com.whatever.biorevive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whatever.biorevive.database.attendance.AttendanceDate
import com.whatever.biorevive.database.attendance.DateWithStudentAttendance
import com.whatever.biorevive.databinding.CustomAttendanceDateBoxBinding

class AttendanceDateAdapter(
    private val attendanceDates:MutableList<DateWithStudentAttendance>,
    val context: Context
):RecyclerView.Adapter<AttendanceDateAdapter.ViewHolder>()
{
    var OnItemClick: ((DateWithStudentAttendance)->Unit)? = null

    inner class ViewHolder(val binding:CustomAttendanceDateBoxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        container: ViewGroup,
        viewType: Int
    ): AttendanceDateAdapter.ViewHolder {
        val binding = CustomAttendanceDateBoxBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceDateAdapter.ViewHolder, position: Int) {
        holder.binding.tvDate.text = attendanceDates[position].attendanceDate.date
        holder.binding.tvAttendanceNumber.text = attendanceDates[position].attendanceDate.numberOfStudentsPresent.toString() + "/48"
        holder.itemView.setOnClickListener{
            OnItemClick?.invoke(attendanceDates[position])
        }
    }

    override fun getItemCount(): Int = attendanceDates.size



}