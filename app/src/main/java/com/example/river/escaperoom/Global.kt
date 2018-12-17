package com.example.river.escaperoom

import android.content.Context
import android.widget.Toast

object Global {
    fun showToast(context: Context, msg:String, duration:Int){
        Toast.makeText(context, msg, duration).show()
    }
}