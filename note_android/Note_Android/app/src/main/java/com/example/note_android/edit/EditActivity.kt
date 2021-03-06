package com.example.note_android.edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.databinding.ActivityEditBinding
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.utils.StatusBarUtils
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import kotlinx.android.synthetic.main.activity_edit.*

@Page(name = "编辑页面")
class EditActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var editBinding: ActivityEditBinding
    var viewModel: EditViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit)
        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        initView()
        initMarkwon()
    }

    private fun initMarkwon() {
        var markwon = Markwon.builder(this).usePlugin(object : AbstractMarkwonPlugin(){
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakHeight(0)
            }
        }).build()
        markwon.setMarkdown(editBinding.showMarkdown,"# 测试")
        
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

    }

}