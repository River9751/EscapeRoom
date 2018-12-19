package com.example.river.escaperoom

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.store.view.*

class StoreAdapter(var list: ArrayList<StoreItem>) : RecyclerView.Adapter<StoreAdapter.CustomViewHolder>() {
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
        }
    }
}