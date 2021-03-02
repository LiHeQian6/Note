package com.example.note_android.welcome

import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.MainActivity
import com.example.note_android.annotation.Page
import com.example.note_android.R
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.sql_lite.QQLoginDbSchema
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.Single
import com.example.note_android.util.StateBarUtils
import com.example.note_android.util.StateUtil
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.XUI
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_welcome.*
import org.json.JSONObject

@Page(name = "欢迎页")
class WelcomeActivity : AppCompatActivity() {

    private lateinit var mTencent: Tencent
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf")
        setContentView(R.layout.activity_welcome)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        initView()
        checkLogin()
    }

    private fun initView(){

        //初始化SQLite数据库
        dataBaseHelper = DataBaseHelper(this,1)
        sqLiteDatabase = dataBaseHelper.readableDatabase

        //设置欢迎页动画
        val ani = AlphaAnimation(0.2f,1.0f)
        ani.duration = 1000
        welcome_bac.startAnimation(ani)

        //启动欢迎页倒计时异步任务
        val a = CustomAsyncTask()
        a.execute()
        wel_close_button.setOnClickListener(View.OnClickListener {
            ActivityUtil.get()?.goActivityKill(this@WelcomeActivity,MainActivity::class.java)
            a.cancel(true)
        })
    }

    private fun checkLogin() {
        var editor = Single.getShared(this)
        var loginType = editor?.getString(resources.getString(R.string.Login_Type),"")
        StateUtil.initInfo(this)
        if (loginType.equals("") ||
            StateUtil.LOGIN_INFO==null ||
            StateUtil.USER_INFO == null) {
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