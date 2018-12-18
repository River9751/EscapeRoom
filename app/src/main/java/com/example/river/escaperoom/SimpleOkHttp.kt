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

    fun get(url: String) {
        //val body = RequestBody.create(JSON, json)

        val builder = Request.Builder()
            .url(url)
            .header("Accept", "application/json")

//        //加入 token
//        builder.header("Authorization", "Bearer $token")
//        builder.post(body)

        val request = builder.build()


        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = if (e.message == "timeout") "連線逾時" else "網路或伺服器異常"
//                Handler(ctx.mainLooper).post {
//                    requestListener.onFailure(true, 0, message)
//                }
                Log.d("onFailure Exception", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()!!
                println(responseString)
                Handler(ctx.mainLooper).post {
                    //                    if(response.code() == 401) sendBroacast(context as BaseActivitiy)
//                    else if (response.code() in 399..500) {
//                        requestListener.onFailure(false, response.code(), responseString)
//                        Log.d("onFailure", responseString)
//                    }
//                    else {
//                        requestListener.onSuccess(responseString, idList)
//                    }
                }
            }
        })
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
                val responseString = response.body()!!.string()
                Log.d("Response", responseString)
                val jsonObject = JSONObject(responseString)
                Handler(ctx.mainLooper).post {
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