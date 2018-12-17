package com.example.river.escaperoom.Dialogs

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import com.example.river.escaperoom.Global
import java.util.*

class ClockDialog {

    val mContext: Context
    val timePickerDialog: TimePickerDialog

    constructor(context: Context, eCaller: E_ClockCaller) {
        mContext = context

        var hour = 0
        var min = 0

        when (eCaller) {
            E_ClockCaller.FROM_CLOCK -> {
                hour = Global.clockHour
                min = Global.clockMinute
            }
            E_ClockCaller.FROM_PHONE -> {
                hour = Global.phoneHour
                min = Global.phoneMinute
            }
        }

        timePickerDialog = TimePickerDialog(
            context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    //Global.showToast(mContext, "${hourOfDay}, ${minute}", Toast.LENGTH_SHORT)
                    when (eCaller) {
                        E_ClockCaller.FROM_CLOCK -> {
                            Global.clockHour = hourOfDay
                            Global.clockMinute = minute
                        }
                        E_ClockCaller.FROM_PHONE -> {
                            Global.phoneHour = hourOfDay
                            Global.phoneMinute = minute
                        }
                    }
                }
            }, hour, min, true
        )

    }

    fun show() {
        timePickerDialog.show()
    }
}