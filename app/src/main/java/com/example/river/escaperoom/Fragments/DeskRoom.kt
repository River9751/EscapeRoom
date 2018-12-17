package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.river.escaperoom.Dialogs.DrawerOpenDialog
import com.example.river.escaperoom.Dialogs.PhotoDialog
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import kotlinx.android.synthetic.main.fragment_deskroom.view.*


class DeskRoom : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_deskroom, container, false)

        view.drawer.setOnClickListener {
            DrawerOpenDialog(activity as MainActivity).show()
        }

        view.photo.setOnClickListener {
            PhotoDialog(activity as MainActivity).show()
        }


        view.floatingLeft.setOnClickListener {
            (activity as MainActivity).switchContent("DeskRoom", "MainRoom")
        }

        view.floatingRight.setOnClickListener {
            (activity as MainActivity).switchContent("DeskRoom", "ClockRoom")
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
    }
}