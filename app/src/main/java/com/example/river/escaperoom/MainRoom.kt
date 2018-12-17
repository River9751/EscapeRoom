package com.example.river.escaperoom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MainRoom : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_mainroom, container, false)
        return view//super.onCreateView(inflater, container, savedInstanceState)
    }
}