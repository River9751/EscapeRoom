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

    constructor(context: Context) {
        mContext = context

        //Add TimePicker
//        timePickerDialog.setContentView()
//        timePickerDialog = Dialog(context)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 8)

        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(
            context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    Global.showToast(mContext, "${hourOfDay}, ${minute}", Toast.LENGTH_SHORT)
                }
            }, hourOfDay, min, true
        )

    }

    fun show() {
        timePickerDialog.show()
    }
}