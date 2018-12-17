package com.example.river.escaperoom.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.river.escaperoom.R

class PhotoDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.photo)
    }
}