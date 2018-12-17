package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.Dialogs.ClockDialog
import com.example.river.escaperoom.Dialogs.LockDialog
import com.example.river.escaperoom.Dialogs.PhoneDialog
import com.example.river.escaperoom.Global
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import kotlinx.android.synthetic.main.fragment_mainroom.view.*

class MainRoom : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_mainroom, container, false)

        view.lock.setOnClickListener {
            if (Global.isReadyToGo()) {
                LockDialog((activity as MainActivity)).show()
            } else {
                Global.showToast((activity as MainActivity), "門鎖住了，無法打開...", Toast.LENGTH_SHORT)
            }
        }

        view.phone.setOnClickListener {
            PhoneDialog(activity as MainActivity).show()
        }

        view.floatingLeft.setOnClickListener {
            (activity as MainActivity).switchContent("MainRoom", "ClockRoom")
        }

        view.floatingRight.setOnClickListener {
            (activity as MainActivity).switchContent("MainRoom", "DeskRoom")
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
    }
}