package com.example.note_android.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.R
import com.example.note_android.setting.fragment.ChangeFragment
import com.example.note_android.setting.fragment.SettingFragment
import com.example.note_android.util.MessageBean
import com.example.note_android.util.StateBarUtils

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        var page = intent.getSerializableExtra("Option") as MessageBean
        when(page.message){
            "UserMessage" -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.settings_fragment, ChangeFragment.newInstance())
                        .commit()
            }
//            "Setting" -> {
//                supportFragmentManager.beginTransaction()
//                        .replace(R.id.settings_fragment, SettingFragment.newInstance())
//                        .commit()
//            }
        }
    }
}