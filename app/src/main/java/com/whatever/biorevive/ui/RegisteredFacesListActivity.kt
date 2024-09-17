package com.whatever.biorevive.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatever.biorevive.database.face.FaceDatabaseManager
import com.whatever.biorevive.databinding.ActivityRegisteredFacesListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisteredFacesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisteredFacesListBinding
    private lateinit var adapter: RegisteredFacesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisteredFacesListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.rvRegisteredFaces.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with a placeholder callback
        adapter = RegisteredFacesListAdapter(
            registeredFaceList = mutableListOf(),
            context = this
        ) { selectedItems ->
            binding.btnDeleteRegistered.isEnabled = selectedItems.isNotEmpty()
        }

        binding.rvRegisteredFaces.adapter = adapter

        // Load data into the adapter
        CoroutineScope(Dispatchers.IO).launch {
            val registeredFaces = FaceDatabaseManager(applicationContext).faceDatabase.faceDao.getFaceData()
            withContext(Dispatchers.Main) {
                adapter.registeredFaceList.clear()
                adapter.registeredFaceList.addAll(registeredFaces)
                adapter.notifyDataSetChanged()
            }
        }

        // Long-click to enable selection mode
        binding.rvRegisteredFaces.setOnLongClickListener {
            adapter.exitSelectionMode()
            true
        }

        // Button to delete selected items
        binding.btnDeleteRegistered.setOnClickListener {
            adapter.deleteSelectedItems()

        }
    }
}