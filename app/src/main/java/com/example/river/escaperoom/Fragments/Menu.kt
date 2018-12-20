package com.example.river.escaperoom.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import com.example.river.escaperoom.Dialogs.AccomplishDialog
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
                msg += "消費 -10, 時間：${i.time} \n"
            }
            Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
        }

        view.accomplish.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("token", sp.getToken())
            jsonObject.put("game", "escapeRoom")
            SimpleOkHttp(activity as MainActivity).post(
                "/api/profile", jsonObject.toString(),
                null,
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        val items = arrayListOf<String>()

                        val responseObj = jsonObject.getJSONObject("response")
                        val email = responseObj.getString("email")
                        val points = responseObj.getString("RemainingPoint")
                        val accomplish = responseObj.getJSONObject("Accomplishment")
                        val hasFindLittleMan = accomplish.getString("FindLittleMan")
                        val hasYouAreFilthyRich = accomplish.getString("YouAreFilthyRich")
                        val hasYouAreSoFast = accomplish.getString("YouAreSoFast")

                        if (hasFindLittleMan == "true") {
                            items.add("FindLittleMan!\n找到 20 個小矮人！")
                        }
                        if (hasYouAreFilthyRich == "true") {
                            items.add("YouAreFilthyRich!\n單次儲值金額超過$2000！")
                        }
                        if (hasYouAreSoFast == "true") {
                            items.add("YouAreSoFast!\n短時間內迅速過關！")
                        }
                        AccomplishDialog(
                            (activity as MainActivity),
                            email,
                            points,
                            items
                        ).show()
                        //Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
                    }

                    override fun onFailure(msg: String) {
                        Global.showToast(activity as MainActivity, msg, Toast.LENGTH_LONG)
                    }
                })
        }

        view.logout.setOnClickListener {
            //更新 token 為空字串
            sp.saveToken("")
            (activity as MainActivity).switchContent("Menu", "Signin")
        }

        view.store.setOnClickListener {
            val intent = Intent()

//            return@setOnClickListener
            //可以登入應該有 Token
            val token = SimpleSharedPreference(activity as MainActivity).getToken()
            val jsonObject = JSONObject()
            jsonObject.put("token", token)
            jsonObject.put("game", "escapeRoom")

            SimpleOkHttp(activity as MainActivity).post(
                "/api/items",
                jsonObject.toString(),
                token,
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        val intent = Intent()
                        intent.putExtra("jsonString", jsonObject.toString())
                        intent.setClass(activity as MainActivity, StoreActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(msg: String) {
                        Global.showToast(activity as MainActivity, msg, Toast.LENGTH_SHORT)
                    }
                }
            )


        }
        return view
    }
}