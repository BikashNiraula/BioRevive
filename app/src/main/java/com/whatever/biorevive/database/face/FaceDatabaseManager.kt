package com.whatever.biorevive.database.face

import android.content.Context
import androidx.room.Room

class FaceDatabaseManager(context: Context) {
    val faceDatabase: FaceDatabase
    init {
        faceDatabase = Room.databaseBuilder(
            context,
            FaceDatabase::class.java,
            "my_face_database"
        )
            .build()
    }

}