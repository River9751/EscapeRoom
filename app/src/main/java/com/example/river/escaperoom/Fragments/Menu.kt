package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.MainActivity
import com.example.river.escaperoom.R
import com.example.river.escaperoom.SimpleSharedPreference
import kotlinx.android.synthetic.main.fragment_menu.view.*


class Menu : Fragment() {

    lateinit var sp : SimpleSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = SimpleSharedPreference(activity as MainActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater
            .from(activity as MainActivity)
            .inflate(R.layout.fragment_menu, container, false)

        view.start.setOnClickListener {
            sp.spentMoney()
            sp.addPlayRecord()
            (activity as MainActivity).startCountDown()
            (activity as MainActivity).switchContent("Menu","MainRoom")
        }

        view.history.setOnClickListener {
            var msg = ""
            for (i in sp.getRecords()) {
                msg += "餘額：${i.deposit}, 時間：${i.time} \n"
            }

            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }

        return view
    }


}