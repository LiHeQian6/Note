package com.example.note_android.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.note_android.login.bean.UserInfo
import com.example.note_android.R
import com.example.note_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    var loginBinding:ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        var user = UserInfo("admin","admin")
        loginBinding!!.user = user
    }
}