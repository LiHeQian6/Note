package com.example.note_android.Welcome

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.Annotation.Page
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_welcome.*

@Page(name = "欢迎页")
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.debug(true)
        StatusBarUtils.translucent(window)
        setContentView(R.layout.activity_welcome)
        val ani = AlphaAnimation(0.2f,1.0f)
        ani.duration = 1000
        welcome_bac.startAnimation(ani)

        val a = CustomAsyncTask()
        a.execute()

        wel_close_button.setOnClickListener(View.OnClickListener {
            val go = Intent()
            go.setClass(applicationContext, MainActivity::class.java)
            startActivity(go)
            finish()
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
            var go = Intent()
            go.setClass(this@WelcomeActivity, MainActivity::class.java)
            this@WelcomeActivity.startActivity(go)
            this@WelcomeActivity.finish()
        }
    }
}