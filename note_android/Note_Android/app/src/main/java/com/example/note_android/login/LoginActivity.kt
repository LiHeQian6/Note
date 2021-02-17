package com.example.note_android.login

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.note_android.MainActivity
import com.example.note_android.login.bean.UserInfo
import com.example.note_android.R
import com.example.note_android.databinding.ActivityLoginBinding
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.RequestCodeUtil
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var loginBinding:ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_button ->{
                ActivityUtil.get()?.goActivityKill(this,MainActivity::class.java)
            }
            R.id.icon_qq_login ->{
                
            }
        }
    }
}