package com.example.note_android.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.databinding.ActivityLoginBinding
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.login.bean.UserInfo
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.StateUtil
import com.tencent.tauth.Tencent


@Page(name = "登录页")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var loginBinding:ActivityLoginBinding? = null
    var mTencent: Tencent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        initView()
        initListener()
    }

    private fun initView(){
        var user = UserInfo("admin","admin")
        loginBinding!!.user = user
    }

    private fun initListener(){
        loginBinding?.loginButton?.setOnClickListener(this)
        loginBinding?.iconQqLogin?.setOnClickListener(this)
    }

    private fun QQLogin(){
        if (!mTencent!!.isSessionValid) {
            mTencent!!.login(this, "all", MyIUiListener(this, "login"))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode,resultCode,data, MyIUiListener(this, "login"))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_button ->{
                ActivityUtil.get()?.goActivityKill(this,MainActivity::class.java)
            }
            R.id.icon_qq_login ->{
                QQLogin()
            }
        }
    }
}