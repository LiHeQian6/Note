package com.example.note_android.login

import android.os.Handler
import android.os.Message
import android.widget.Button
import java.util.*

class CountDownTimer(var button: Button): Runnable {

    private var handler = Handler()
    private var count:Int = 30

    override fun run() {
        if(count >= 1) {
            button.isClickable = false
            handler.postDelayed(this, 1000)
            button.text = count.toString()
            count--
        }else{
            handler.removeCallbacks(this)
            button.isClickable = true
            button.text = "获 取"
        }
    }
}