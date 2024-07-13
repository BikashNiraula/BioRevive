package com.whatever.biorevive.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FaceData(
    val name:String,
     val rollNo:String,

     val faceEmbedding:FloatArray,
    @PrimaryKey(true)
    val id:Int = 0
)


