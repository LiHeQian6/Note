package com.example.note_android.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.login.bean.UserInfo
import com.example.note_android.util.*
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Page(name = "登录页")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mTencent: Tencent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        EventBus.getDefault().register(this)
        //initView()
        initListener()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun initLoginInfo(loginEvent: LoginEvent){
        if (loginEvent != null) {
            mTencent.saveSession(loginEvent.p1)
            EventBus.getDefault().unregister(this)
            finish()
        }
    }

    private fun initListener(){
        login_button.setOnClickListener(this)
        icon_qq_login.setOnClickListener(this)
        btn_to_register.setOnClickListener(this)
        btn_forget_password.setOnClickListener(this)
    }

    private fun QQLogin(){
        if (!mTencent.isSessionValid) {
            mTencent.login(this, "all", MyIUiListener(applicationContext,"login"))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SystemCode.QQ_LOGIN_REQUEST -> {
                Tencent.onActivityResultData(requestCode,resultCode,data, MyIUiListener(applicationContext,"login"))
            }
            SystemCode.REGISTER -> {
                if(data?.extras?.get("result").toString() == "Success") {
                    var email = data?.extras?.get("email").toString()
                    var password = data?.extras?.get("password").toString()
                    login_email.setText(email)
                    login_password.setText(password)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_button ->{
                ActivityUtil.get().goActivityKill(this,MainActivity::class.java)
            }
            R.id.icon_qq_login ->{
                //QQLogin()
            }
            R.id.btn_to_register -> {
                ActivityUtil.get().goActivityResult(this,RegisterActivity::class.java,SystemCode.REGISTER)
            }
            R.id.btn_forget_password -> {
                ActivityUtil.get().goActivityResult(this,ForgetPassActivity::class.java,SystemCode.FORGET_PASSWORD)
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}