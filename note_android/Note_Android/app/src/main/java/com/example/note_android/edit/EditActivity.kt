package com.example.note_android.edit

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.databinding.ActivityEditBinding
import com.example.note_android.edit.model.EditListener
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.edit.viewmodel.EditViewModel
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_edit.*

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity(),View.OnClickListener {
    var editBinding: ActivityEditBinding? = null
    var viewModel: EditViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit)
        StatusBarUtils.initStatusBarStyle(this,true,resources.getColor(R.color.orange))

        initView()
        initRichEditor()
    }

    private fun initRichEditor() {
        richEditor.setPlaceholder("从这里开始你的内容...")
        text_bold.setOnClickListener(this)
        text_lean.setOnClickListener(this)
        head_1.setOnClickListener(this)
        head_2.setOnClickListener(this)
        head_3.setOnClickListener(this)
        head_4.setOnClickListener(this)
        head_5.setOnClickListener(this)
        head_6.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.text_bold -> {
                richEditor.setBold()
            }
            R.id.text_lean -> {
                richEditor.setItalic()
            }
            R.id.head_1 -> {
                richEditor.setHeading(1)
            }
            R.id.head_2 -> {
                richEditor.setHeading(2)
            }
            R.id.head_3 -> {
                richEditor.setHeading(3)
            }
            R.id.head_4 -> {
                richEditor.setHeading(4)
            }
            R.id.head_5 -> {
                richEditor.setHeading(5)
            }
            R.id.head_6 -> {
                richEditor.setHeading(6)
            }
        }
    }

}