package com.example.river.escaperoom.Fragments.Member

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.river.escaperoom.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import org.json.JSONObject


class Signup : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater
            .from(activity as MainActivity)
            .inflate(R.layout.fragment_signup, container, false)

        view.cancel.setOnClickListener {
            (activity as MainActivity).switchContent("Signup", "Signin")
        }

        view.signUpBtnConfirm.setOnClickListener {
            /*
            Register:
{
    "email" : "123@gmail.com.tw",
    "password":"YourPassword",
    "password_confirmation":"YourPasswordConfirmation"
}
            * */

            val email = signUpAcc.text.toString()
            val password = signUpPwd.text.toString()
            val password_confirmation = signUpConfirm.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)
            jsonObject.put("password_confirmation", password_confirmation)

            val body = jsonObject.toString()

            SimpleOkHttp((activity as MainActivity)).post("/api/register",
                body, null, object : IResponse {
                    override fun onSuccess(jsonObject: JSONObject) {
                        Global.showToast(
                            (activity as MainActivity),
                            jsonObject.getString("response"),
                            Toast.LENGTH_SHORT
                        )
                        (activity as MainActivity).switchContent("Signup", "Signin")
                    }

                    override fun onFailure(msg: String) {
                        Global.showToast(
                            (activity as MainActivity),
                            msg,
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            )
        }

        return view
    }

}