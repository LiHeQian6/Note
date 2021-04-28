package com.example.note_android.welcome

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.MainActivity
import com.example.note_android.annotation.Page
import com.example.note_android.R
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_welcome.*
import org.json.JSONObject

@Page(name = "欢迎页")
class WelcomeActivity : AppCompatActivity() {

    private lateinit var mTencent: Tencent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
//        XUI.initFontStyle("fonts/hwxk.ttf")
        setContentView(R.layout.activity_welcome)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        initView()
        checkLogin()
    }

    private fun initView(){
        //设置欢迎页动画
        val ani = AlphaAnimation(0.2f,1.0f)
        ani.duration = 1000
        welcome_bac.startAnimation(ani)


        //启动欢迎页倒计时异步任务
        val a = CustomAsyncTask()
        a.execute()
        wel_close_button.setOnClickListener(View.OnClickListener {
            ActivityUtil.get().goActivityKill(this@WelcomeActivity,MainActivity::class.java)
            a.cancel(true)
        })
    }

    private fun checkLogin() {
        var shared = getSharedPreferences(resources.getString(R.string.Login_Type),Context.MODE_PRIVATE)
        var loginType = shared?.getString(resources.getString(R.string.Login_Type),"")
        StateUtil.AUTHORIZATION = shared?.getString(resources.getString(R.string.Authorization),"").toString()
        StateUtil.AUTHORIZATION = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NTk2ODQ1ODFAcXEuY29tIiwiY3JlYXRlZCI6MTYxOTU5OTA1NDA3Mywicm9sZXMiOiJST0xFX1VTRVIiLCJleHAiOjE2MjAyMDM4NTR9.4ygTDVAKwiqgEGcK9JgI5PFcPArkrT-AAnn9MzTqPvHujMuBv8XFjK9by977xrLnyo6IayWstWlj0b4Bzms49w"
        StateUtil.AUTHORIZATION_HEADERS = shared?.getString(resources.getString(R.string.Authorization_Header),"").toString()
        StateUtil.initInfo(this)
        if (loginType.equals("") ||
            StateUtil.LOGIN_INFO==null ||
            StateUtil.USER_INFO == null || StateUtil.AUTHORIZATION_HEADERS == "") {
            StateUtil.IF_LOGIN = false
        }else{
            StateUtil.IF_LOGIN = true
            StateUtil.LOGIN_TYPE = loginType!!
            mTencent.openId = StateUtil.LOGIN_INFO?.openid
            mTencent.setAccessToken(StateUtil.LOGIN_INFO?.access_token,StateUtil.LOGIN_INFO?.expires_in)
            mTencent.checkLogin(object : IUiListener {
                override fun onComplete(p0: Any?) {
                    p0 as JSONObject
                    if (p0.optInt("ret", -1) == 0) {
                        mTencent.loadSession(resources.getString(R.string.APP_ID));
                    } else {
                        StateUtil.IF_LOGIN = false
                    }
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onWarning(p0: Int) {
                    TODO("Not yet implemented")
                }

                override fun onError(p0: UiError?) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private inner class CustomAsyncTask : AsyncTask<String, Int, Int>(){
        override fun doInBackground(vararg params: String?): Int {
            var i=5
            while (i in 5 downTo 1 && !isCancelled) {
                publishProgress(i--)
                Thread.sleep(1000)
            }
            return i
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            wel_close_button.text = "关闭 ${values.get(0)}"
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
                ActivityUtil.get()?.goActivityKill(this@WelcomeActivity,MainActivity::class.java)
        }
    }
}