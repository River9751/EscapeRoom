package com.example.river.escaperoom

import android.content.Context
import android.widget.Toast
import java.util.*

object Global {

    var phoneHour = 0
    var phoneMinute = 0
    var clockHour = 0
    var clockMinute = 0

    var starCount = 5f

    init {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 8)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        phoneHour = hourOfDay
        phoneMinute = min

        clockHour = hourOfDay
        clockMinute = min
    }

    fun showToast(context: Context, msg: String, duration: Int) {
        Toast.makeText(context, msg, duration).show()
    }

    fun isReadyToGo():Boolean{
        if (phoneHour == 11 && phoneMinute == 48 && clockHour == 20 && clockMinute == 18){
            return true
        }
        return false
    }
}