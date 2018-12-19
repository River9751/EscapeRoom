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
import org.json.JSONObject

class FinishScreen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_finish, container, false)

        view.button3.setOnClickListener {
            (activity as MainActivity).switchContent("FinishScreen", "Menu")
        }

        return view
    }

    fun setStar(){
        ratingBar.numStars = 5
        ratingBar.rating = Global.starCount
        Global.starCount = 5f

        //
        if (Global.starCount < 4.5f){
            return
        }
        /*
        {
    "token": "yourToken","game":"escapeRoom", "accomplishment":"YouAreSoFast"
}
         */
        val token = SimpleSharedPreference(activity as MainActivity).getToken() ?: ""
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("game", "escapeRoom")
        jsonObject.put("accomplishment", "YouAreSoFast")

        SimpleOkHttp(activity as MainActivity).post(
            "/api/personalAccomplishment",
            jsonObject.toString(),
            token,
            object : IResponse {
                override fun onSuccess(jsonObject: JSONObject) {
                    Global.showToast(
                        activity as MainActivity,
                        jsonObject.getString("response"),
                        Toast.LENGTH_LONG
                    )
                }

                override fun onFailure(msg: String) {
                    Log.d("onFailure", msg)
                }
            })
    }


}