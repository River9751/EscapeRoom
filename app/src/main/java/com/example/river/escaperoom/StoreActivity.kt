package com.example.river.escaperoom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_items.*

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        var id = 0

        if (Global.viewAllItem != null) {
            id = Global.viewAllItem!!.id
        }

        val list = arrayListOf<StoreItem>()
        list.add(StoreItem(1, "顯示所有線索位置！", 100, R.drawable.view_all, Global.viewAllItem != null))
//        list.add(StoreItem(2, "顯示所有線索位置！", 200, R.drawable.view_all, true))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StoreAdapter(this, list)
    }
}