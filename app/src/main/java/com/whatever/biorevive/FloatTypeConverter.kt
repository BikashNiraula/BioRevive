package com.whatever.biorevive

import androidx.room.TypeConverter


class FloatTypeConverter {

    @TypeConverter
    fun stringToFloatArray(str:String):FloatArray{
        val strValues = str.split(",").toTypedArray()
        return strValues.map { it.toFloat() }.toFloatArray()
    }
    @TypeConverter
    fun floatArrayToString(floatArray: FloatArray): String {
        return floatArray.joinToString()
    }
}