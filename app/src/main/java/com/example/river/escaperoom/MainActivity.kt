package com.example.river.escaperoom


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.river.escaperoom.Fragments.MainRoom
import com.example.river.escaperoom.Fragments.Menu
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var menu: Menu
    private lateinit var mainRoom: MainRoom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val sp = SimpleSharedPreference(this)
        //main room
        //touch area

        //隱藏倒數計時
        countDown.visibility = View.INVISIBLE

        initFragments()

        /**
         * Clock * 2
         * NumberLock * 2
         * Rating Bar * 1
         */
    }


    /**
     * 初始化所有 Fragment 實體
     */
    private fun initFragments() {
        //建立實體
        currentFragment = Fragment()
        menu = Menu()
        mainRoom = MainRoom()

        //加入 tag
        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, menu, "Menu")
        trans.add(R.id.container, mainRoom, "MainRoom")

        //顯示第一個 Fragment
        trans.hide(mainRoom).show(menu).commit()
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

    lateinit var countDownTimer: CountDownTimer
    fun startCountDown() {
        countDown.visibility = View.VISIBLE

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

                countDown.text = ms
            }
        }
        countDownTimer.start()
    }

    fun gameOver() {
        Global.showToast(this, "Game Over!", Toast.LENGTH_LONG)
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
                switchContent("MainRoom", "Menu")
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}