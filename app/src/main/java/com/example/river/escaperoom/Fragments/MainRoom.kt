package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import com.example.river.escaperoom.Dialogs.DwarfDialog
import com.example.river.escaperoom.Dialogs.LockDialog
import com.example.river.escaperoom.Dialogs.PhoneDialog
import kotlinx.android.synthetic.main.fragment_mainroom.view.*
import org.json.JSONObject

class MainRoom : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_mainroom, container, false)

        if (Global.viewAllItem != null){
            view.lock.setImageResource(R.drawable.eye)
            view.phone.setImageResource(R.drawable.eye)
        }

        view.lock.setOnClickListener {
            if (Global.isReadyToGo()) {
                LockDialog((activity as MainActivity)).show()
            } else {
                Global.showToast((activity as MainActivity), "門鎖住了，無法打開...", Toast.LENGTH_SHORT)
            }
        }

        view.dwarf.setOnClickListener {
            DwarfDialog(activity as MainActivity).show()
            /*
            River:
{
    "token": "yourToken",
    "game": "escapeRoom"
    "accomplishment": "findTheLittleMan"
}
             */
            val token = SimpleSharedPreference(activity as MainActivity).getToken() ?: ""
            val jsonObject = JSONObject()
            jsonObject.put("token", token)
            jsonObject.put("game", "escapeRoom")
            jsonObject.put("accomplishment", "findTheLittleMan")

            SimpleOkHttp(activity as MainActivity).post(
                "/api/findTheLittleMan",
                jsonObject.toString(),
                token,
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        if (jsonObject.has("findTheLittleMan")) {
                            Global.showToast(
                                activity as MainActivity,
                                jsonObject.getString("findTheLittleMan"),
                                Toast.LENGTH_LONG
                            )
                        }
                        Log.d("Dwarf", "Found one Dwarf")
                    }

                    override fun onFailure(msg: String) {
                        Log.d("Dwarf", "Found Dwarf Error $msg")
                    }

                })
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
}