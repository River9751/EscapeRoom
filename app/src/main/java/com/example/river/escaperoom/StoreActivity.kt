package com.example.river.escaperoom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_items.*
import org.json.JSONObject

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)


        Global.showToast(this, Global.purchased.toString(), Toast.LENGTH_SHORT)
//        var id = 0
//
//        if (Global.viewAllItem != null) {
//            id = Global.viewAllItem!!.id
//        }


        val jsonString = intent.getStringExtra("jsonString")
        val jsonObject = JSONObject(jsonString)
        val response = jsonObject.getJSONObject("response")

        val list = arrayListOf<StoreItem>()
        list.add(
            StoreItem(
                1, "顯示所有線索位置！", 100, R.drawable.view_all,
                !Global.purchased
               // !response.has("viewAll")
            )
        )
        //Global.viewAllItem!!.purchased))
//        list.add(StoreItem(2, "顯示所有線索位置！", 200, R.drawable.view_all, true))

        val adapter = StoreAdapter(this, list)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()
    }
}