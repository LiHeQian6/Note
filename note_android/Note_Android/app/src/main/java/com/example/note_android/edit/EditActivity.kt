package com.example.note_android.edit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.databinding.ActivityEditBinding
import com.example.note_android.edit.model.EditListener
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.edit.viewmodel.EditViewModel
import kotlinx.android.synthetic.main.activity_edit.*

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity() {
    var editBinding: ActivityEditBinding? = null
    var viewModel: EditViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit)

        richEditor.setPlaceholder("输入")
        EditListener(richEditor)

        text_bold.setOnClickListener(EditListener(richEditor))

        initView()

    }

    private fun initView() {
        SoftKeyBoardListener.setListener(this,object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                input_tool.animate().translationY((-height).toFloat()).setDuration(300).start()
//                Toast.makeText(this@EditActivity, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }

            override fun keyBoardHide(height: Int) {
                input_tool.animate().translationY((0).toFloat()).setDuration(0).start()
//                Toast.makeText(this@EditActivity, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        })
    }

}