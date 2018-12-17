package com.example.river.escaperoom

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

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

        initFragments()


        /**
         * Clock * 2
         * NumberLock * 2
         *
         */
    }

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