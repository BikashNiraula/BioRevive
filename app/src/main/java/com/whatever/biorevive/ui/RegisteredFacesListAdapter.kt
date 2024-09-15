package com.whatever.biorevive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whatever.biorevive.database.attendance.StudentAttendance
import com.whatever.biorevive.database.face.FaceData
import com.whatever.biorevive.databinding.CustomAttendanceBoxBinding
import com.whatever.biorevive.databinding.CustomRegisterFaceListBoxBinding

class RegisteredFacesListAdapter(
    private val registeredFaceList: MutableList<FaceData>,
    val context: Context
) : RecyclerView.Adapter<RegisteredFacesListAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: CustomRegisterFaceListBoxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomRegisterFaceListBoxBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = registeredFaceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = registeredFaceList[position].name
        holder.binding.tvRollNo.text = registeredFaceList[position].rollNo
        holder.binding.tvSerialNo.text = (position + 1).toString() // Serial number typically starts from 1


    }
}
