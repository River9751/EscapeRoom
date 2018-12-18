package com.example.river.escaperoom

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.river.escaperoom.Fragments.*
import com.example.river.escaperoom.Fragments.Member.Signin
import com.example.river.escaperoom.Fragments.Member.Signup
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var menu: Menu
    private lateinit var mainRoom: MainRoom
    private lateinit var deskRoom: DeskRoom
    private lateinit var clockRoom: ClockRoom
    private lateinit var finishScreen: FinishScreen
    private lateinit var failScreen: FailScreen
    private lateinit var signin: Signin
    private lateinit var signup: Signup


    /**
     * TimePicker * 2
     * NumberPicker * 2
     * RatingBar * 1
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.visibility = View.INVISIBLE //避免畫面先出現
        //隱藏倒數計時
        countDown.visibility = View.INVISIBLE

        initFragments()

        addFragmentTags()

        checkLoginStatus()
    }

    /**
     * 初始化所有 Fragment 實體
     */
    private fun initFragments() {
        //建立實體
        currentFragment = Fragment()
        menu = Menu()
        mainRoom = MainRoom()
        deskRoom = DeskRoom()
        clockRoom = ClockRoom()
        finishScreen = FinishScreen()
        failScreen = FailScreen()
        signin = Signin()
        signup = Signup()
    }

    private fun addFragmentTags() {

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, menu, "Menu")
        trans.add(R.id.container, mainRoom, "MainRoom")
        trans.add(R.id.container, deskRoom, "DeskRoom")
        trans.add(R.id.container, clockRoom, "ClockRoom")
        trans.add(R.id.container, finishScreen, "FinishScreen")
        trans.add(R.id.container, failScreen, "FailScreen")
        trans.add(R.id.container, signin, "Signin")
        trans.add(R.id.container, signup, "Signup")

        trans.commit()
    }

    private fun checkLoginStatus() {
        val token = SimpleSharedPreference(this).getToken()
        if (token == null) {
            showFirstPage(false)
            return
        }
        //檢查 token
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        SimpleOkHttp(this).post("/api/profile", jsonObject.toString(), null, object : IResponse {
            override fun onSuccess(jsonObject: JSONObject) {
                //把成就清單設定到 Global
//                val responseObj = jsonObject.getJSONObject("response")
//                val accomplish = responseObj.getJSONObject("Accomplishment")
//                val hasFindLittleMan = accomplish.getString("FindLittleMan")
//                val hasYouAreFilthyRich = accomplish.getString("YouAreFilthyRich")
//                if (hasFindLittleMan == "true") {
//                    Global.accomplishmentList.add(Accomplishment("FindLittleMan"))
//                }
//                if (hasYouAreFilthyRich == "true") {
//                    Global.accomplishmentList.add(Accomplishment("YouAreFilthyRich"))
//                }

                showFirstPage(true)
            }

            override fun onFailure(msg: String) {
                showFirstPage(false)
            }
        })
    }

    fun showFirstPage(passLogin: Boolean) {
        //顯示第一個 Fragment
        val trans = supportFragmentManager.beginTransaction()
        trans
            .hide(mainRoom)
            .hide(deskRoom)
            .hide(clockRoom)
            .hide(finishScreen)
            .hide(failScreen)
            .hide(signup)

        if (passLogin) {
            trans
                .hide(signin)
                .show(menu).commit()
        } else {
            trans
                .hide(menu)
                .show(signin).commit()
        }
        container.visibility = View.VISIBLE
    }


    /**
     * 切換內容頁
     */
    fun switchContent(tagFrom: String, tagTo: String) {
        val from = supportFragmentManager.findFragmentByTag(tagFrom)!!
        val to = supportFragmentManager.findFragmentByTag(tagTo)!!

        if (currentFragment !== to) {
            currentFragment = to
            val trans =
                supportFragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out
                )

            if (!to.isAdded) {
                trans.hide(from).add(R.id.container, to, tagTo).commit()
            } else {
                trans.hide(from).show(to).commit()
            }
        }
    }


    /**
     * 倒數計時器
     */
    private lateinit var countDownTimer: CountDownTimer

    fun startCountDown() {
        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onFinish() {
                gameOver()
            }

            override fun onTick(millisUntilFinished: Long) {
                val ms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                            ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                Global.starCount = millisUntilFinished.toFloat() / 300000 * 5
                countDown.text = ms
            }
        }
        countDownTimer.start()
        countDown.visibility = View.VISIBLE
    }

    fun stopCountDown() {
        countDownTimer.cancel()
        countDown.visibility = View.INVISIBLE
    }

    fun gameOver() {
        Global.showToast(this, "Game Over!", Toast.LENGTH_LONG)
    }

    fun setRatingStar() {
        val fg = supportFragmentManager.findFragmentByTag("FinishScreen")
        (fg as FinishScreen).setStar()
    }

    /**
     * 處理返回鍵
     */
    override fun onBackPressed() {
        when (currentFragment.tag) {
            "Menu" -> {
                super.onBackPressed()
            }
            "MainRoom" -> {
                stopCountDown()
                switchContent("MainRoom", "Menu")
            }
            "Signup" -> {
                switchContent("Signup", "Signin")
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}