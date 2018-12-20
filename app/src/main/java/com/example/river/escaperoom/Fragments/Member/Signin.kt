package com.example.river.escaperoom.Fragments.Member

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import kotlinx.android.synthetic.main.fragment_signin.view.*
import org.json.JSONObject
import java.lang.Exception


class Signin : Fragment() {

    lateinit var sp: SimpleSharedPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater
            .from(activity as MainActivity)
            .inflate(R.layout.fragment_signin, container, false)

        sp = SimpleSharedPreference(activity as MainActivity)

        view.signInBtn.setOnClickListener {
            val email = view.signInAcc.text.toString()
            val password = view.signInPwd.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            val token = sp.getToken()

            SimpleOkHttp(activity as MainActivity).post(
                "/api/login",
                jsonObject.toString(),
                token,
                object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        sp.saveToken(jsonObject["token"].toString())
                        Log.d("Token", jsonObject["token"].toString())
                        getData(jsonObject["token"].toString())

                    }

                    override fun onFailure(msg: String) {
                        Global.showToast(activity as MainActivity, msg, Toast.LENGTH_SHORT)
                    }
                }
            )
        }

        view.signUpBtn.setOnClickListener {
            //            sp.spentMoney()
//            sp.addPlayRecord()
            (activity as MainActivity).switchContent("Signin", "Signup")
        }

        return view
    }

    fun getData(token: String) {
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("game", "escapeRoom")
        println("Token $token")
        SimpleOkHttp(activity as MainActivity).post(
            "/api/profile",
            jsonObject.toString(),
            null, object : IResponse {
                override fun onSuccess(jsonObject: JSONObject) {
                    //成功拿到 Profile
                    val response = jsonObject.getJSONObject("response")
                    try {
                        val purchasedItems = response.getJSONArray("PurchasedItems")
                        Global.purchased = purchasedItems.length() > 0
                    } catch (ex: Exception) {
                        Global.purchased = false
                    }
                    //Global.showToast(activity as MainActivity, Global.purchased.toString(), Toast.LENGTH_SHORT)
                    (activity as MainActivity).switchContent("Signin", "Menu")
                }

                override fun onFailure(msg: String) {
                    //失敗回到登入頁
                    Global.showToast(activity as MainActivity, msg, Toast.LENGTH_SHORT)
                }
            })
    }
}