package com.example.note_android.welcome

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.*
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.MainActivity
import com.example.note_android.annotation.Page
import com.example.note_android.R
import com.example.note_android.bean.Note
import com.example.note_android.bean.NoteType
import com.example.note_android.bean.UserInfo
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_welcome.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

@Page(name = "欢迎页")
class WelcomeActivity : AppCompatActivity() {

    private lateinit var mTencent: Tencent
    private var a:CustomAsyncTask = CustomAsyncTask()
    private lateinit var handler: Handler
    private var gson:Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
//        XUI.initFontStyle("fonts/hwxk.ttf")
        setContentView(R.layout.activity_welcome)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        initView()
        checkLogin()
        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    var userInfo:UserInfo = gson.fromJson(result.get("data").toString(),UserInfo::class.java)
                    StateUtil.SYSTEM_USER_INFO = userInfo
                    StateUtil.IF_LOGIN = true
                }else
                    StateUtil.IF_LOGIN = false
                a.execute()
                wel_close_button.setOnClickListener(View.OnClickListener {
                    ActivityUtil.get().goActivityKill(this@WelcomeActivity,MainActivity::class.java)
                    a.cancel(true)
                })
            }
        }
    }

    private fun initView(){
        //设置欢迎页动画
        val ani = AlphaAnimation(0.2f,1.0f)
        ani.duration = 1000
        welcome_bac.startAnimation(ani)
    }

    private fun checkLogin() {
        var shared = getSharedPreferences(resources.getString(R.string.Login_Type),Context.MODE_PRIVATE)
        StateUtil.AUTHORIZATION = shared?.getString(resources.getString(R.string.Authorization),"").toString()
        StateUtil.AUTHORIZATION = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NTk2ODQ1ODFAcXEuY29tIiwiY3JlYXRlZCI6MTYxOTk1ODk0MTY1OSwicm9sZXMiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYyMDU2Mzc0MX0.o5B9U3q1D1eYSzGPUGA41uPFiyuLoEJO4RaMogx3s9QIOVJ1JjTuomUIIL5op0GIItpefWHYlz3g62mSdsczqQ"
        StateUtil.AUTHORIZATION_HEADERS = shared?.getString(resources.getString(R.string.Authorization_Header),"").toString()
        if (StateUtil.AUTHORIZATION == "") {
            StateUtil.IF_LOGIN = false
        }else{
            StateUtil.IF_LOGIN = true
            getUserInfo()
        }
    }

    private fun getUserInfo(){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getUserInfo().toHttpUrlOrNull()!!.newBuilder()
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(this@WelcomeActivity,"网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@WelcomeActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                handler.sendMessage(mess)
            }
        })
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