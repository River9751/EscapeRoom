package com.example.river.escaperoom

import android.content.Context
import android.os.Handler
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class SimpleOkHttp {

    private val JSON = MediaType.get("application/json; charset=utf-8")

    private val ctx: Context

    constructor(ctx: Context) {
        this.ctx = ctx
    }

    fun post(url: String, json: String, token: String?, callback: IResponse) {

        val body = RequestBody.create(JSON, json)

        val builder = Request.Builder()
            .url(Global.baseURL + url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")

        //加入 token
        if (token != null) {
            builder.header("Authorization", "Bearer $token")
        }

        builder.post(body)

        val request = builder.build()

        val client = OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("onFailure", e.message)
                Handler(ctx.mainLooper).post {
                    callback.onFailure(e.message!!)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()!!
                Log.d("Response", responseString)

                Handler(ctx.mainLooper).post {
                    if(responseString == ""){
                        callback.onFailure("No Response")
                        return@post
                    }
                    val jsonObject = JSONObject(responseString)
                    if (jsonObject.getString("result") == "true") {
                        callback.onSuccess(jsonObject)
                    } else {
                        callback.onFailure(jsonObject.getString("response"))
                    }
                }
            }
        })
    }
}

interface IResponse {
    fun onSuccess(jsonObject: JSONObject)
    fun onFailure(msg: String)
}