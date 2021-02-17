package com.example.note_android.welcome

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.MainActivity
import com.example.note_android.annotation.Page
import com.example.note_android.login.LoginActivity
import com.example.note_android.R
import com.example.note_android.util.ActivityUtil
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_welcome.*

@Page(name = "欢迎页")
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.debug(true)
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf");
        setContentView(R.layout.activity_welcome)
        val ani = AlphaAnimation(0.2f,1.0f)
        ani.duration = 1000
        welcome_bac.startAnimation(ani)

        val a = CustomAsyncTask()
        a.execute()

        wel_close_button.setOnClickListener(View.OnClickListener {
            ActivityUtil.get()?.goActivityKill(this,LoginActivity::class.java)
            a.cancel(true)
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
            ActivityUtil.get()?.goActivityKill(this@WelcomeActivity,LoginActivity::class.java)
        }
    }
}