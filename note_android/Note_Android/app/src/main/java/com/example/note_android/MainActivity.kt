package com.example.note_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.note_android.Annotation.Page
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.StatusBarUtils

@Page(name = "主界面")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        XUI.init(application)
        XUI.debug(true)
        StatusBarUtils.translucent(window)
    }
}