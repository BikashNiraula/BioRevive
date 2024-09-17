package com.whatever.biorevive.database.face

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaceData(faceData: FaceData)

    @Delete
    suspend fun deleteFaceData(faceData: FaceData)

    @Delete
    suspend fun deleteFaceDataList(faceDataList: List<FaceData>)

    @Query("SELECT * from facedata ORDER BY rollNo ASC")
    suspend fun getFaceData():List<FaceData>
}