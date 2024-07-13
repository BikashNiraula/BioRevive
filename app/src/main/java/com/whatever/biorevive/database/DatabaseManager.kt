package com.whatever.biorevive.database

import android.content.Context
import androidx.room.Room

class DatabaseManager(context: Context) {
    val database: FaceDatabase
    init {
        database = Room.databaseBuilder(
            context,
            FaceDatabase::class.java,
            "my_database"
        )
            .build()
    }

}