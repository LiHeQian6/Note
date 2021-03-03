package com.example.note_android.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.note_android.R
import com.example.note_android.databinding.ActivityForgetPassBinding
import com.example.note_android.login.adapter.VPAdapter
import com.example.note_android.login.fragment.ForgetFragment
import com.xuexiang.xui.XUI

class ForgetPassActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityForgetPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf")
        initFragment()
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_forget_pass)
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.forget_fragment,ForgetFragment())
            .commit()
    }
}