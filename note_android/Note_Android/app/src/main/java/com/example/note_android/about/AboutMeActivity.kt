package com.example.note_android.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.note_android.R
import com.example.note_android.about.fragment.my_favourite.MyLikeFragment
import com.example.note_android.about.fragment.my_like_up.MyClickUpFragment
import com.example.note_android.about.fragment.my_note.MyNoteFragment
import com.example.note_android.util.MessageBean
import com.example.note_android.util.StateBarUtils

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        var page = intent.getParcelableExtra<MessageBean>("Option")
        when(page?.message){
            "MyNote" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_about_me,MyNoteFragment.newInstance())
                    .commit()
            }
            "MyLike" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_about_me,MyLikeFragment.newInstance())
                    .commit()
            }
            "MyClickUp" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_about_me,MyClickUpFragment.newInstance())
                    .commit()
            }
        }
    }
}