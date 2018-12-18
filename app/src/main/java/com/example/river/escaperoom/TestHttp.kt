package com.example.river.escaperoom

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object TestHttp {


    private val JSON = MediaType.get("application/json; charset=utf-8")

    private var client = OkHttpClient()

    enum class RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    fun post(context: Context, needPostToken: Boolean, url: String, json: String, requestListener: IResponse) {
        val body = RequestBody.create(JSON, json)
        request(RequestMethod.POST, context, needPostToken, url, body, null, requestListener)
    }


    private fun request(
        requestMethod: RequestMethod,
        context: Context,
        needPostToken: Boolean,
        url: String,
        body: RequestBody?,
        idList: MutableList<Int>?,
        requestListener: IResponse
    ) {


        val builder = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        when (requestMethod) {
            RequestMethod.POST -> {
                if (needPostToken) { //需要 Token
                    //builder.header("Authorization", "Bearer ${SharePreferenceUtil.token}")
                }
                builder.post(body!!)
            }
        }

        val request = builder.build()

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(context.mainLooper).post {
                    requestListener.onFailure(e.message!!)
                }
                Log.d("onFailure Exception", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()!!
                Log.d("onResponse", responseString)

                Handler(context.mainLooper).post {
                    var jsonObject = JSONObject(responseString)
                    if (jsonObject.getString("result") == "false") {
                        requestListener.onFailure(jsonObject.getString("response"))
                        Log.d("onFailure", responseString)
                    } else {
                        requestListener.onSuccess(jsonObject)
                    }
                }
            }
        })
    }

}