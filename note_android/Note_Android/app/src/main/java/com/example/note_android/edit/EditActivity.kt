package com.example.note_android.edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.databinding.ActivityEditBinding
import com.example.note_android.edit.fragment.EditNoteFragment
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.login.fragment.ForgetFragment
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.utils.StatusBarUtils
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import kotlinx.android.synthetic.main.activity_edit.*

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity() {
    private lateinit var editBinding: ActivityEditBinding
    var viewModel: EditViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        initFragment()
    }

    private fun initFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_edit_note, EditNoteFragment())
            .commit()
    }
}