package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.river.escaperoom.Global
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import kotlinx.android.synthetic.main.fragment_finish.*
import kotlinx.android.synthetic.main.fragment_finish.view.*

class FinishScreen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_finish, container, false)

//        view.ratingBar.numStars = 5
//        view.ratingBar.rating = 4.5f

        view.button3.setOnClickListener {
            (activity as MainActivity).switchContent("FinishScreen", "Menu")
        }

        return view
    }

    fun setStar(){
        ratingBar.numStars = 5
        ratingBar.rating = Global.starCount
        Global.starCount = 5f
    }
}