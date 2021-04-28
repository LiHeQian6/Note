package com.example.note_android.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.util.*
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_person.view.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.io.IOException
import java.util.*


@Page(name = "登录页")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mTencent: Tencent
    private lateinit var handler: Handler
    private lateinit var share: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID), applicationContext)
        EventBus.getDefault().register(this)
        share = getSharedPreferences(resources.getString(R.string.LoginInfo),Context.MODE_PRIVATE).edit()
        //initView()
        getImageCode("")
        initListener()
        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result.get("status") == "SUCCESS")
                    ActivityUtil.get().goActivityKill(this@LoginActivity, MainActivity::class.java)
                else
                    Toast.makeText(this@LoginActivity,result.get("message").toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImageCode(random:String) {
        Glide.with(this).load(HttpAddressUtil.getImageCode()+random)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .signature(ObjectKey(System.currentTimeMillis()))
                .dontAnimate()
                .into(verify_img)
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
        verify_img.setOnClickListener(this)
    }

    private fun login(userName:String,passWord:String,verifyCode:String){
        val httpClient = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("username", userName)
                .add("password", passWord)
                .add("imageCode", verifyCode)
                .build();
        val request = Request.Builder().url(HttpAddressUtil.login()).post(formBody).build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(applicationContext,"网络出了点问题",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@LoginActivity,"数据出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var authorHeaders = response.headers["Access-Control-Expose-Headers"]
                var authorization = response.headers["Authorization"]
                share.putString(resources.getString(R.string.Authorization),authorization)
                share.putString(resources.getString(R.string.Authorization_Header),authorHeaders)
                share.commit()
                StateUtil.IF_LOGIN = true
                StateUtil.AUTHORIZATION = authorization?:""
                StateUtil.AUTHORIZATION_HEADERS = authorHeaders?:""
                var mess = Message()
                mess.obj = result
                mess.what = 0
                handler.sendMessage(mess)
            }
        })
    }

    private fun QQLogin(){
        if (!mTencent.isSessionValid) {
            mTencent.login(this, "all", MyIUiListener(applicationContext, "login"))
        }
    }

    private fun checkNull(userName:String,passWord:String,verifyCode:String):Boolean{
        if (userName.equals("") || passWord.equals("") || verifyCode.equals("")) {
            Toast.makeText(this@LoginActivity,"请输入信息", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SystemCode.QQ_LOGIN_REQUEST -> {
                Tencent.onActivityResultData(requestCode, resultCode, data, MyIUiListener(applicationContext, "login"))
            }
            SystemCode.REGISTER -> {
                if (data?.extras?.get("result").toString() == "Success") {
                    var email = data?.extras?.get("email").toString()
                    var password = data?.extras?.get("password").toString()
                    login_email.setText(email)
                    login_password.setText(password)
                    getImageCode("")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_button -> {
                if (checkNull(
                                login_email.text.toString(),
                                login_password.text.toString(),
                                login_verify.text.toString()
                        )) {
                    login(login_email.text.toString(), login_password.text.toString(), login_verify.text.toString())
                }
            }
            R.id.icon_qq_login -> {
                //QQLogin()
            }
            R.id.btn_to_register -> {
                ActivityUtil.get().goActivityResult(this, RegisterActivity::class.java, SystemCode.REGISTER)
            }
            R.id.btn_forget_password -> {
                ActivityUtil.get().goActivityResult(this, ForgetPassActivity::class.java, SystemCode.FORGET_PASSWORD)
            }
            R.id.verify_img -> {
                getImageCode("")
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}