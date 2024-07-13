package com.whatever.biorevive.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whatever.biorevive.FloatTypeConverter

@Database(
    entities = [FaceData::class],
    version = 1
)
@TypeConverters(FloatTypeConverter::class)
abstract class FaceDatabase:RoomDatabase() {

    abstract val dao: FaceDao
}