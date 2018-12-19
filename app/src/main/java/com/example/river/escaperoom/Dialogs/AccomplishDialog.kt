package com.example.river.escaperoom.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.river.escaperoom.R
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.accomplish.*


class AccomplishDialog(
    val ctx: Context,
    val email: String,
    val points: String,
    val items: ArrayList<String>
) : Dialog(ctx) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.accomplish)


        emailText.text = "User: $email"
        pointsText.text = "Points: $points"

        val itemsAdapter = ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, items)
        listView.adapter = itemsAdapter
    }
}