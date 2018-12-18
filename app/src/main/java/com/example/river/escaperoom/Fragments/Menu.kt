package com.example.river.escaperoom.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import kotlinx.android.synthetic.main.fragment_menu.view.*
import org.json.JSONObject


class Menu : Fragment() {

    lateinit var sp: SimpleSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = SimpleSharedPreference(activity as MainActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater
            .from(activity as MainActivity)
            .inflate(R.layout.fragment_menu, container, false)

        /*
        {
    "token": "yourToken",
    "game": "escapeRoom"
}
         */
        view.start.setOnClickListener {
            //            sp.spentMoney()
            val jsonObject = JSONObject()
            jsonObject.put("token", sp.getToken())
            jsonObject.put("game", "escapeRoom")
            SimpleOkHttp(activity as MainActivity).post(
                "/api/deduction",
                jsonObject.toString(),
                sp.getToken() ?: "",
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        sp.addPlayRecord()
                        (activity as MainActivity).startCountDown()
                        (activity as MainActivity).switchContent("Menu", "MainRoom")
                    }

                    override fun onFailure(msg: String) {
                        Global.showToast((activity as MainActivity), msg, Toast.LENGTH_LONG)
                    }
                }
            )
        }

        view.history.setOnClickListener {
            var msg = ""
            for (i in sp.getRecords()) {
                msg += "餘額：${i.deposit}, 時間：${i.time} \n"
            }
            Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
        }

        view.accomplish.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("token", sp.getToken())
            SimpleOkHttp(activity as MainActivity).post(
                "/api/profile", jsonObject.toString(),
                null,
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        var msg = ""
                        val responseObj = jsonObject.getJSONObject("response")
                        val email = responseObj.getString("email")
                        msg += "$email：\n"
                        val points = responseObj.getString("RemainingPoint")
                        msg += "餘額：$points\n"
                        val accomplish = responseObj.getJSONObject("Accomplishment")
                        val hasFindLittleMan = accomplish.getString("FindLittleMan")
                        val hasYouAreFilthyRich = accomplish.getString("YouAreFilthyRich")
                        if (hasFindLittleMan == "true") {
                            msg += "FindLittleMan!\n"
                        }
                        if (hasYouAreFilthyRich == "true") {
                            msg += "YouAreFilthyRich!\n"
                        }
                        Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
                    }

                    override fun onFailure(msg: String) {
                        Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
                    }
                })
        }

        return view
    }


}