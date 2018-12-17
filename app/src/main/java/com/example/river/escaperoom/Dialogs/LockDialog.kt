package com.example.river.escaperoom.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.river.escaperoom.Global
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import kotlinx.android.synthetic.main.lock.*

class LockDialog(var ctx: Context) : Dialog(ctx) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lock)

        numberPicker1.minValue = 0
        numberPicker1.maxValue = 9
        numberPicker2.minValue = 0
        numberPicker2.maxValue = 9

        button2.setOnClickListener {
            if (numberPicker1.value == 1 && numberPicker2.value == 0) {
//                (ctx as MainActivity).gameFinish()
                this.hide()
                (ctx as MainActivity).stopCountDown()
                (ctx as MainActivity).setRatingStar()
                (ctx as MainActivity).switchContent("MainRoom", "FinishScreen")
            } else {
                Global.showToast(ctx, "失敗！", Toast.LENGTH_SHORT)
            }
        }
    }
}