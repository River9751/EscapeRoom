package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.Dialogs.ClockDialog
import com.example.river.escaperoom.Global
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import kotlinx.android.synthetic.main.fragment_mainroom.view.*

class MainRoom : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_mainroom, container, false)

        view.drawer.setOnClickListener {
            Global.showToast((activity as MainActivity), "門鎖住了，無法打開...", Toast.LENGTH_SHORT)
            //ClockDialog((activity as MainActivity)).show()
        }
        //val view = TestCustomView(activity as MainActivity, null)
        return view//super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
    }
}