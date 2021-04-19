package com.example.note_android.edit.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.note_android.R
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.xuexiang.xui.widget.dialog.DialogLoader
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import kotlinx.android.synthetic.main.fragment_edit_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class EditNoteFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_edit_note,container,false)
        initTool()
        initEditor()
        initView()
        return rootView
    }

    private fun initEditor() {
        var markwon = Markwon.builder(requireContext()).usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakHeight(0)
            }
        }).build()
        var editor = MarkwonEditor.create(markwon)
        rootView.edit_markdown.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor))
    }

    private fun initView() {
        rootView.preview_note.setOnClickListener(this)
        rootView.publish_note.setOnClickListener(this)
        rootView.mk_head.setOnClickListener(this)
        rootView.mk_bold.setOnClickListener(this)
        rootView.mk_list.setOnClickListener(this)
        rootView.edit_markdown.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                rootView.edit_note_subtitle_text.text = "最近保存 " + getSystemTime()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun getSystemTime(): String {
        var simpleDateFormat = SimpleDateFormat("HH:mm:ss");
        //获取当前时间戳
        var date = Date(System.currentTimeMillis());
        return simpleDateFormat.format(date)
    }

    private fun initTool() {
        SoftKeyBoardListener.setListener(requireActivity(),object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                rootView.input_tool.animate().translationY((-height).toFloat()).setDuration(300).start()
//                Toast.makeText(this@EditActivity, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }
            override fun keyBoardHide(height: Int) {
                rootView.input_tool.animate().translationY((0).toFloat()).setDuration(0).start()
//                Toast.makeText(this@EditActivity, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.preview_note -> {
                var manager =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(rootView.edit_markdown.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .add(R.id.fragment_edit_note,PreviewNoteFragment(rootView.edit_markdown.text.toString()))
                    .commit()
            }
            R.id.publish_note -> {
                DialogLoader.getInstance().showConfirmDialog(
                    requireContext(),
                    "确定发布这篇文章吗",
                    "发布",
                    { dialog: DialogInterface?, which: Int ->
                        //TODO 去发布
                    },
                    "再想想",
                    { dialog: DialogInterface, which: Int ->
                        dialog.dismiss()
                    }
                )
            }
            R.id.mk_head -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"#")
            }
            R.id.mk_bold -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"****")
                rootView.edit_markdown.setSelection(rootView.edit_markdown.text.length - 2)
            }
            R.id.mk_list -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"-")
            }
        }
    }
}