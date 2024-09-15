package com.whatever.biorevive.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatever.biorevive.R
import com.whatever.biorevive.database.face.FaceDatabase
import com.whatever.biorevive.database.face.FaceDatabaseManager
import com.whatever.biorevive.databinding.ActivityRegisteredFacesListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisteredFacesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisteredFacesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisteredFacesListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.rvRegisteredFaces.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.Main).launch {
            val registeredFaces =
                FaceDatabaseManager(applicationContext).faceDatabase.faceDao.getFaceData()

            val customAdapter = RegisteredFacesListAdapter(registeredFaces.toMutableList(), this@RegisteredFacesListActivity)
            binding.rvRegisteredFaces.layoutManager = LinearLayoutManager(this@RegisteredFacesListActivity)
            binding.rvRegisteredFaces.adapter = customAdapter
        }



    }
}