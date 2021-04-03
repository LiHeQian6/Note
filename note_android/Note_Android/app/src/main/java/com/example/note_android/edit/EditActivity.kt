package com.example.note_android.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.edit.fragment.EditNoteFragment
import com.example.note_android.util.StateBarUtils

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity() {
    var viewModel: EditViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        initFragment()
    }

    private fun initFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_edit_note, EditNoteFragment())
            .commit()
    }
}