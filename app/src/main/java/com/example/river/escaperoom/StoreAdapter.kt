package com.example.river.escaperoom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.store.view.*
import org.json.JSONObject

class StoreAdapter(val ctx: Context, var list: ArrayList<StoreItem>) :
    RecyclerView.Adapter<StoreAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.store, p0, false)
        return CustomViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, position: Int) {
        customViewHolder.bind(position)
    }

    inner class CustomViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) {
            v.title.text = "${list[position].title}\n$${list[position].cost}"
            v.itemImage.setImageResource(list[position].imageId)
            v.purchase.isEnabled = !list[position].purchased
            v.purchase.text = if (list[position].purchased) "Purchased" else "Purchase"
//            v.purchase.isEnabled = !Global.purchased
//            v.purchase.text = if (Global.purchased) "Purchased" else "Purchase"

            v.purchase.setOnClickListener {
                doPurchase(list[position].id)
            }
        }
    }

    fun doPurchase(id: Int) {
        /*
        {
    "token": "yourToken", "id": 1 (string)
}
         */
        val token = SimpleSharedPreference(ctx).getToken()!!

        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("item", id)
        SimpleOkHttp(ctx).post("/api/purchase", jsonObject.toString(), null, object : IResponse {
            override fun onSuccess(jsonObject: JSONObject) {
                val msg = jsonObject.getString("remainingPoints")
                Global.showToast(ctx, "購買成功！,餘額$msg", Toast.LENGTH_SHORT)
                val target = list.first { x -> x.id == id }
                target.purchased = true
                Global.purchased = true
                //Global.viewAllItem = StoreItem(id,"",100,R.drawable.view_all,true)
                notifyDataSetChanged()
            }

            override fun onFailure(msg: String) {
                Global.showToast(ctx, msg, Toast.LENGTH_SHORT)
            }

        })
    }
}