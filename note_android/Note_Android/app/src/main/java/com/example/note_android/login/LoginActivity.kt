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
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.UserInfo
import com.example.note_android.util.*
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject


@Page(name = "登录页")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var loginBinding:ActivityLoginBinding? = null
    var mTencent: Tencent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        EventBus.getDefault().register(this)
        initView()
        initListener()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun initLoginInfo(loginEvent: LoginEvent){
        if (loginEvent != null) {
            mTencent?.saveSession(loginEvent.p1)
            EventBus.getDefault().unregister(this)
            finish()
        }
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
            mTencent!!.login(this, "all", MyIUiListener(applicationContext,"login"))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode,resultCode,data, MyIUiListener(applicationContext,"login"))

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