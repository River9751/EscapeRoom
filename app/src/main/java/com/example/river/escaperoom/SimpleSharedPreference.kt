package com.example.river.escaperoom

import android.content.Context
import android.content.SharedPreferences

import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class SimpleSharedPreference {

    private val mPreferencesName: String = "EscapeRoomPreferences"
    private val mPreferences: SharedPreferences
    private val mContext: Context
    private val recordKey = "RecordKey"
    private val depositKey = "DepositKey"

    constructor(context: Context) {
        this.mContext = context
        mPreferences = context.getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE)
        checkFirstInstall()
    }

    fun checkFirstInstall() {
        if (mPreferences.getInt(depositKey, -1) == -1) {
            val editor = mPreferences.edit()
            //餘額初始值
            editor.putInt(depositKey, 500)
            //歷史初始值
            editor.putString(recordKey, "[]")

            editor.apply()
            Toast.makeText(mContext, "+500", Toast.LENGTH_LONG).show()
        }
    }


    fun getRecords(): ArrayList<PlayRecord> {

        //[ { "id" : 1, "cash" : 500, "time" : "2018xxx" } ]
        val jsonArrayString = mPreferences.getString(recordKey, "[]")
        val records = JSONArray(jsonArrayString)

        val list = arrayListOf<PlayRecord>()
        for (i in 0 until records.length()) {
            val jsonObject = records.getJSONObject(i)
            val cash = jsonObject.getInt("deposit")
            val time = jsonObject.getString("time")
            list.add(PlayRecord(cash, time))
        }

        return list
    }

    fun getDeposit(): Int {
        return mPreferences.getInt(depositKey, 0)
    }

    fun addPlayRecord() {
        val calendar = Calendar.getInstance()
        val time = DateFormat.format("yyyy-MM-dd kk:mm:ss", calendar.time)

        val jsonObject = JSONObject()

        //先拿出目前的 Array
        val jsonArrayString = mPreferences.getString(recordKey, "[]")
        val jsonArray = JSONArray(jsonArrayString)

        val deposit = getDeposit()
        jsonObject.put("deposit", deposit)
        jsonObject.put("time", time)

        jsonArray.put(jsonObject)

        val editor = mPreferences.edit()
        editor.putString(recordKey, jsonArray.toString())
        editor.apply()
    }

    fun spentMoney() {
        //更新餘額
        var deposit = mPreferences.getInt(depositKey, 0)
        if (deposit == 0) deposit = 500
        val editor = mPreferences.edit()
        editor.putInt(depositKey, deposit - 10)
        editor.apply()
    }


    fun delete(uniqueId: String) {
//        val editor = mPreferences.edit()
//        try {
//            editor.remove(uniqueId)
//            editor.apply()
//            cb.onSuccess(uniqueId)
//        } catch (ex: Exception) {
//            cb.onError(ex.message!!)
//        }
    }


}

data class PlayRecord(val deposit: Int, val time: String)