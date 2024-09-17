package com.whatever.biorevive.ui

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.whatever.biorevive.database.face.FaceData
import com.whatever.biorevive.database.face.FaceDatabaseManager
import com.whatever.biorevive.databinding.CustomRegisterFaceListBoxBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisteredFacesListAdapter(
    val registeredFaceList: MutableList<FaceData>,
    private val context: Context,
    private val onSelectionChanged: (selectedItems: Set<FaceData>) -> Unit
) : RecyclerView.Adapter<RegisteredFacesListAdapter.ViewHolder>() {

    private var selectionMode = false
    private val selectedItems = mutableSetOf<FaceData>()

    inner class ViewHolder(val binding: CustomRegisterFaceListBoxBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Handle checkbox clicks to toggle selection
            binding.chkBoxDelete.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(registeredFaceList[adapterPosition])
                } else {
                    selectedItems.remove(registeredFaceList[adapterPosition])
                }
                onSelectionChanged(selectedItems)
            }

            // Handle item long click to enable selection mode
            binding.root.setOnLongClickListener {
                if (!selectionMode) {
                    selectionMode = true
                    notifyDataSetChanged()
                }
                true
            }

            // Handle item clicks to toggle checkbox in selection mode
            binding.root.setOnClickListener {
                if (selectionMode) {
                    binding.chkBoxDelete.isChecked = !binding.chkBoxDelete.isChecked
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomRegisterFaceListBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faceData = registeredFaceList[position]
        holder.binding.tvName.text = faceData.name
        holder.binding.tvRollNo.text = faceData.rollNo
        holder.binding.tvSerialNo.text = (position + 1).toString()
        holder.binding.chkBoxDelete.visibility = if (selectionMode) View.VISIBLE else View.GONE
        holder.binding.chkBoxDelete.isChecked = selectedItems.contains(faceData)
        holder.itemView.setBackgroundColor(if (selectedItems.contains(faceData)) Color.LTGRAY else Color.TRANSPARENT)
    }

    override fun getItemCount(): Int = registeredFaceList.size

    fun exitSelectionMode() {
        selectionMode = false
        selectedItems.clear()
        Handler(Looper.getMainLooper()).post {
            notifyDataSetChanged()
        }
    }

    fun deleteSelectedItems() {

        if(selectedItems.isNotEmpty()){
            val dialog = AlertDialog.Builder(context)
            .setTitle("!!!Delete Students!!!")
            .setMessage("Are you sure that you want to delete registered students?")
            .setPositiveButton("Yes"){_,_->
                   //delete items
                   CoroutineScope(Dispatchers.IO).launch {
                        FaceDatabaseManager(context.applicationContext).faceDatabase.faceDao.deleteFaceDataList(
                            selectedItems.toList()
                            )
                            registeredFaceList.removeAll(selectedItems)
                            exitSelectionMode()
                        }
                    }
              .setNeutralButton("Cancel"){_,_->

              }
              .setCancelable(false)
                .show()
     }



    }
}
