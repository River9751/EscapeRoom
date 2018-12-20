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
    private lateinit var gameOverScreen: GameoverScreen
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
        gameOverScreen = GameoverScreen()
        signin = Signin()
        signup = Signup()
    }

    /**
     * Fragment 加入 Tag
     */
    private fun addFragmentTags() {

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, menu, "Menu")
        trans.add(R.id.container, mainRoom, "MainRoom")
        trans.add(R.id.container, deskRoom, "DeskRoom")
        trans.add(R.id.container, clockRoom, "ClockRoom")
        trans.add(R.id.container, finishScreen, "FinishScreen")
        trans.add(R.id.container, gameOverScreen, "GameoverScreen")
        trans.add(R.id.container, signin, "Signin")
        trans.add(R.id.container, signup, "Signup")

        trans.commit()
    }

    /**
     *
     */
    private fun checkLoginStatus() {
        val token = SimpleSharedPreference(this).getToken()
        //沒有 token 進入登入頁面
        if (token == null || token == "") {
            showFirstPage(false)
            return
        }
        //直接進入選單頁面
        // 1. 儲存 Profile 資訊 2. 儲存道具資訊
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("game", "escapeRoom")
        println("Token $token")
        SimpleOkHttp(this).post(
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

                    showFirstPage(true)
                }

                override fun onFailure(msg: String) {
                    //失敗回到登入頁
                    Global.showToast(this@MainActivity, msg, Toast.LENGTH_SHORT)

                    showFirstPage(false)
                }
            })
    }

    fun requestItems() {
        val token = SimpleSharedPreference(this).getToken()
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("game", "escapeRoom")

        SimpleOkHttp(this).post(
            "/api/items",
            jsonObject.toString(),
            token,
            object : IResponse {
                override fun onSuccess(jsonObject: JSONObject) {
//                    val list = arrayListOf<StoreItem>()
                    /*
                    {
    "result": "true",
    "response": {
        "email": "tn710617ab@gmail.com",
        "RemainingPoint": 0,
        "Accomplishment": {
            "FindLittleMan": "true",
            "YouAreFilthyRich": "false"
        }
    }
}
                     */
                    //第一樣道具
                    val response = jsonObject.getJSONObject("response")
                    if (response.has("viewAll")) {
                        val viewAll = response.getJSONObject("viewAll")
                        val cost = viewAll.getInt("cost")
                        val id = viewAll.getInt("id")
                        Global.purchased = true

                        //list.add(StoreItem(id, "顯示所有線索！", cost, R.drawable.view_all))
                        Global.viewAllItem = StoreItem(
                            id,
                            "顯示所有線索！",
                            cost,
                            R.drawable.view_all,
                            true
                        )
                    }

                    //profile 和 道具都拿完，進入選單頁面
                    showFirstPage(true)
                }

                override fun onFailure(msg: String) {
                    Global.showToast(this@MainActivity, msg, Toast.LENGTH_SHORT)
                    showFirstPage(false)
                }
            }
        )

    }

    fun showFirstPage(passLogin: Boolean) {
        //顯示第一個 Fragment
        val trans = supportFragmentManager.beginTransaction()
        trans
            .hide(mainRoom)
            .hide(deskRoom)
            .hide(clockRoom)
            .hide(finishScreen)
            .hide(gameOverScreen)
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
                to.onResume()
            } else {
                trans.hide(from).show(to).commit()
                to.onResume()
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
                stopCountDown()
                switchContent(currentFragment.tag!!, "GameoverScreen")
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

    fun setRatingStar() {
        val fg = supportFragmentManager.findFragmentByTag("FinishScreen")
        (fg as FinishScreen).setStar()
    }

    /**
     * 處理返回鍵
     */
    override fun onBackPressed() {
        when (currentFragment.tag) {
            "Signup" -> {
                switchContent("Signup", "Signin")
            }
            "MainRoom" -> {
                stopCountDown()
                switchContent("MainRoom", "Menu")
            }
            "DeskRoom" -> {
                stopCountDown()
                switchContent("DeskRoom", "Menu")
            }
            "ClockRoom" -> {
                stopCountDown()
                switchContent("ClockRoom", "Menu")
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}


//class SimpleTimer {
//
//    private constructor() {
//
//    }
//
//    companion object {
//        var mTimer: CountDownTimer? = null
//        fun getTimera():CountDownTimer {
//            if (mTimer == null) {
//                mTimer = object : CountDownTimer(300000, 1000) {
//                    override fun onFinish() {
//                        //gameOver()
//                    }
//
//                    override fun onTick(millisUntilFinished: Long) {
//                        val ms = String.format(
//                            "%02d:%02d",
//                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
//                                    TimeUnit.HOURS.toMinutes(
//                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
//                                    ),
//                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
//                            )
//                        )
//                        Global.starCount = millisUntilFinished.toFloat() / 300000 * 5
//                        //countDown.text = ms
//                    }
//                }
//            }
//            return mTimer
//        }
//    }
//}