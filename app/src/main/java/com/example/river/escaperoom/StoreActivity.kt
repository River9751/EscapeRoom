package com.example.river.escaperoom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_items.*

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        val list = arrayListOf<StoreItem>()
        list.add(StoreItem(1, "FakeItemCCCCCFFC", 100, R.drawable.view_all, true))
        list.add(StoreItem(2, "FakeItem", 100, R.drawable.view_all, Global.viewAllItem != null))


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StoreAdapter(list)
    }
}