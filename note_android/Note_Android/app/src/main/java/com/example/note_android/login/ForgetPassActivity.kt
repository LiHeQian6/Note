package com.example.note_android.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.note_android.R
import com.example.note_android.login.fragment.ForgetFragment
import com.xuexiang.xui.XUI

class ForgetPassActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.forget_fragment,ForgetFragment())
            .commit()
    }
}