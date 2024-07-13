package com.whatever.biorevive.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.whatever.biorevive.R


class InfoDialogBox(context:Context){
    //private lateinit var dialogBinding: DialogBoxBinding
    private val dialog:AlertDialog
    val yesButton:Button
    val cancelButton:Button
    val nameEditText:EditText
    val rollNoEditText:EditText

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_box,null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(view)
        nameEditText = view.findViewById(R.id.edtName)
        rollNoEditText = view.findViewById(R.id.edtRollNo)
        yesButton = view.findViewById(R.id.btnDialogYes)
        cancelButton = view.findViewById(R.id.btnDialogCancel)
        dialog = dialogBuilder.create()

    }

    fun show(){
        dialog.show()
    }
    fun dismiss(){
        dialog.dismiss()
    }



}