package com.example.note_android.edit

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.bean.NoteInfo
import com.example.note_android.edit.fragment.EditNoteFragment
import com.example.note_android.util.StateBarUtils

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity() {
    var viewModel: EditViewModel? = null
    private var currentNote: NoteInfo? = null
    private lateinit var editNoteFragment: EditNoteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        if(intent.getSerializableExtra("Option") != null) {
            currentNote = intent.getSerializableExtra("Option") as NoteInfo
            intent.removeExtra("Option")
        }else
            currentNote = null
        initFragment()
    }

    private fun initFragment(){
        if(currentNote != null)
            editNoteFragment = EditNoteFragment.newInstance(currentNote)
        else
            editNoteFragment = EditNoteFragment.newInstance(null)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_edit_note, editNoteFragment,"editNoteFragment")
            .commit()
    }
}