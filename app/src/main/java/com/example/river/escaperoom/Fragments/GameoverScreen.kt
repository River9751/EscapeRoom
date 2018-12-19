package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import kotlinx.android.synthetic.main.fragment_finish.*
import kotlinx.android.synthetic.main.fragment_finish.view.*
import kotlinx.android.synthetic.main.fragment_gameover.view.*
import org.json.JSONObject

class GameoverScreen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_gameover, container, false)

        view.gameOverBackBtn.setOnClickListener {
            (activity as MainActivity).switchContent("GameoverScreen", "Menu")
        }

        return view
    }
}