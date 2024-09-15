package com.whatever.biorevive.utils

import java.time.LocalDate

fun generateTodayDate():String{
    val localDate = LocalDate.now().toString()
    return localDate
}