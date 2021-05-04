package com.example.note_android.edit.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Scroller
import android.widget.Toast
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.note_android.R
import com.example.note_android.bean.Note
import com.example.note_android.bean.NoteInfo
import com.example.note_android.bean.NoteType
import com.example.note_android.bean.Tag
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.login.CountDownTimer
import com.example.note_android.login.LoginActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.xuexiang.xui.widget.dialog.DialogLoader
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_edit_note.view.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class EditNoteFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var noteTitle: String
    private lateinit var noteContent: String
    private lateinit var handler: Handler
    private var tags:MutableList<Tag> = ArrayList()
    private lateinit var type:NoteType
    private lateinit var gson:Gson
    private lateinit var note:Note
    private lateinit var dialog:Dialog

    companion object{

        private var currentNote: NoteInfo? = null

        fun newInstance(noteInfo: NoteInfo?): EditNoteFragment{
            currentNote = null
            if (noteInfo != null) {
                currentNote = noteInfo
            }else{
                currentNote = null
            }
            return EditNoteFragment();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_edit_note,container,false)
        initNote()
        checkLogin()
        initTool()
        initEditor()
        initView()
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if (result != null && result.get("status") == "SUCCESS"){
                    dialog.dismiss()
                    Toast.makeText(requireContext(),"发布成功", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"网络出了点问题", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return rootView
    }

    private fun initNote() {
        if (currentNote == null)
            return
        var selectedTags = "";
        rootView.edit_note_title.setText(currentNote?.title.toString())
        rootView.select_note_type.text = currentNote?.type?.name
        if(currentNote?.tags?.size!! > 0){
            selectedTags += currentNote?.tags?.get(0)?.name
            for (i in 1 until tags.size){
                selectedTags += "，${currentNote?.tags?.get(i)?.name}"
            }
        }
        type = currentNote?.type!!
        tags = currentNote?.tags!!
        rootView.select_note_tag.text = selectedTags
        rootView.edit_markdown.setText(currentNote?.content)
    }

    private fun checkLogin() {
        if(!StateUtil.IF_LOGIN){
            DialogLoader.getInstance().showConfirmDialog(
                    requireContext(),
                    "请先登录才能编写笔记，是否跳转到登录页面？",
                    "去登陆",
                    { dialog: DialogInterface?, which: Int ->
                        ActivityUtil.get().goActivityKill(requireContext(),LoginActivity::class.java)
                    },
                    "算了吧",
                    { dialog: DialogInterface, which: Int ->
                        requireActivity().finish()
                    }
            )
        }
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
        rootView.select_note_type.setOnClickListener(this)
        rootView.select_note_tag.setOnClickListener(this)

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

    private fun publishNote(){
        var httpClient = OkHttpClient()
        var mediaType = "application/json".toMediaTypeOrNull()
        var json = JSONObject()
        if (currentNote == null)
            note = Note("0",noteTitle,noteContent,tags,type)
        else
            note = Note(currentNote!!.id.toString(),noteTitle,noteContent,tags,type)
        gson = Gson()
        json.put("note", gson.toJson(note))
        var requestBody = RequestBody.create(mediaType,json.get("note").toString())
        val urlBuilder = HttpAddressUtil.note().toHttpUrlOrNull()!!.newBuilder()
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        if (currentNote == null)
            request.method("POST",requestBody)
        else
            request.method("PUT",requestBody)
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(),"网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(requireContext(),"数据出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                handler.sendMessage(mess)
                tags.clear()
            }

        })
    }

    private fun checkData(): Boolean{
        if(noteTitle == "" || tags.size == 0 || type == null){
            Toast.makeText(requireContext(),"请完善文章信息！", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initTool() {
        SoftKeyBoardListener.setListener(requireActivity(),object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                rootView.input_tool.animate().translationYBy((-height).toFloat()).setDuration(300).start()
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
            //预览文章
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
            //发布文章
            R.id.publish_note -> {
                noteTitle = rootView.edit_note_title.text.toString()
                noteContent = rootView.edit_markdown.text.toString()
                dialog = DialogLoader.getInstance().showConfirmDialog(
                    requireContext(),
                    "确定发布这篇文章吗",
                    "发布",
                    { dialog: DialogInterface?, which: Int ->
                        if(checkData())
                            publishNote()
                    },
                    "再想想",
                    { dialog: DialogInterface, which: Int ->
                        dialog.dismiss()
                    }
                )
            }
            R.id.select_note_type -> {
                var manager =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(rootView.edit_markdown.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .add(R.id.fragment_edit_note,SelectTypeFragment("选择类别",HttpAddressUtil.getTypes()))
                        .commit()
            }
            R.id.select_note_tag -> {
                var manager =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(rootView.edit_markdown.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .add(R.id.fragment_edit_note,SelectTypeFragment("选择标签",HttpAddressUtil.getTags()))
                        .commit()
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

    fun setNoteType(noteType: NoteType){
        rootView.select_note_type.text = noteType.name
        type = noteType
        return
    }

    fun setTags(tags:MutableList<Tag>){
        var selectedTags = ""
        if(tags.size > 0){
            selectedTags += tags[0].name
            for (i in 1 until tags.size){
                selectedTags += "，${tags[i].name}"
            }
            rootView.select_note_tag.text = selectedTags
        }
        this.tags = tags
    }
}