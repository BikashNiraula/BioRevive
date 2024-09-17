package com.whatever.biorevive.database.face

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FaceData(
    val name:String,
    @PrimaryKey(autoGenerate = false)
     val rollNo:String,

     val faceEmbedding:FloatArray
)


