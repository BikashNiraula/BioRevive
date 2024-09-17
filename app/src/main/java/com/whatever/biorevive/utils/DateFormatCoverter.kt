package com.whatever.biorevive.utils

fun convertToSuitableDateFormat(date:String):String{
    var year = ""
    var month = ""
    var day = ""
    var noOfDashes = 0
    for(i in date) {
        if(i == '-'){
            noOfDashes++
            continue
        }

        if (noOfDashes == 0) {
            year += i
        } else if (noOfDashes == 1) {
            month += i
        } else if (noOfDashes == 2) {
            day += i
        }
    }

    val dayInt = day.toInt()
    month = when(month){
        "01"->"January"
        "02"->"February"
        "03"->"March"
        "04"->"April"
        "05"->"May"
        "06"->"June"
        "07"->"July"
        "08"->"August"
        "09"->"September"
        "10"->"October"
        "11"->"November"
        "12"->"December"
        else->"Invalid Month"
    }
    return "${month} ${dayInt.toString()},${year}"

}
