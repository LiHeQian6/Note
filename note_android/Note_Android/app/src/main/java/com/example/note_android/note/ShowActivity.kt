package com.example.note_android.note

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.bean.*
import com.example.note_android.edit.fragment.EditNoteFragment
import com.example.note_android.edit.fragment.FlowTagAdapter
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_edit_note.view.*
import kotlinx.android.synthetic.main.fragment_select_type.*
import kotlinx.android.synthetic.main.note_list_item.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class ShowActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var showViewModel: ShowViewModel
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var currentNote: NoteInfo
    private lateinit var newNote: Common
    private var list: MutableList<Comment> = ArrayList()
    private lateinit var newComment:Comment
    private lateinit var markwon: Markwon
    private lateinit var handler: Handler
    private var gson: Gson = Gson()
    private lateinit var miniLoadingDialog: MiniLoadingDialog
    private var IF_FOLLOW: Boolean = false
    private var IF_LIKE: Boolean = false
    private var IF_COLLECT: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        showViewModel = ViewModelProvider(this).get(ShowViewModel::class.java)
        currentNote = intent.getSerializableExtra("Option") as NoteInfo
        miniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this);
        miniLoadingDialog.show()
        initData()
        initListener()
        handler = object : Handler(){
            @RequiresApi(Build.VERSION_CODES.M)
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS") {
                    when(msg.what){
                        0 -> {
                            var userInfo: NoteInfo = gson.fromJson(result.get("data").toString(), NoteInfo::class.java)
                            currentNote = userInfo
                            miniLoadingDialog.dismiss()
                            initView()
                            initExListView()
                        }
                        1 -> {
                            IF_FOLLOW = !IF_FOLLOW
                            if(IF_FOLLOW)
                                Toast.makeText(this@ShowActivity,"关注成功", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this@ShowActivity,"已取消关注", Toast.LENGTH_SHORT).show()
                            changeStatus()
                        }
                        2 -> {
                            IF_LIKE = !IF_LIKE
                            if(IF_LIKE) {
                                current_zan_num.text = (++currentNote.like).toString()
                                ico_dianzan.setColorFilter(resources.getColor(R.color.orange, null))
                            }else {
                                current_zan_num.text = (--currentNote.like).toString()
                                ico_dianzan.setColorFilter(resources.getColor(R.color.little_gray, null))
                            }
                        }
                        3 -> {
                            IF_COLLECT = !IF_COLLECT
                            if(IF_COLLECT) {
                                current_save_num.text = (++currentNote.collect).toString()
                                ico_save.setColorFilter(resources.getColor(R.color.orange, null))
                            }else{
                                current_save_num.text = (--currentNote.collect).toString()
                                ico_save.setColorFilter(resources.getColor(R.color.little_gray,null))
                            }
                        }
                        4 -> {
                            newComment.user = StateUtil.SYSTEM_USER_INFO
                            list.add(newComment)
                            initExListView()
                            var manager =  this@ShowActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            manager.hideSoftInputFromWindow(comment_content.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                            comment_content.setText("")
                            comment_content.clearFocus()
                            Toast.makeText(this@ShowActivity,"评论成功！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@ShowActivity,result.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initListener() {
        follow_this_writer.setOnClickListener(this)
        ico_dianzan.setOnClickListener(this)
        ico_save.setOnClickListener(this)
        publish_comment.setOnClickListener(this)
    }

    private fun initData() {
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.note().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addPathSegment(currentNote.id.toString())
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                miniLoadingDialog.dismiss()
                Toast.makeText(this@ShowActivity,"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@ShowActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 0
                handler.sendMessage(mess)
            }
        })
    }

    private fun follow(){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toFollow().toHttpUrlOrNull()!!.newBuilder()
        var formBody = FormBody.Builder()
        formBody.add("targetId",currentNote.user?.id.toString())
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        if(IF_FOLLOW)
            request.delete(formBody.build())
        else{
            request.post(formBody.build())
        }
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                miniLoadingDialog.dismiss()
                Toast.makeText(this@ShowActivity,"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@ShowActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                handler.sendMessage(mess)
            }
        })
    }

    private fun like(){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toLike().toHttpUrlOrNull()!!.newBuilder()
        var formBody = FormBody.Builder()
        formBody.add("entityId",currentNote.id.toString())
        formBody.add("entityUserId",currentNote.user?.id.toString())
        formBody.add("entityType","1")
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        request.post(formBody.build())
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                miniLoadingDialog.dismiss()
                Toast.makeText(this@ShowActivity,"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@ShowActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 2
                handler.sendMessage(mess)
            }
        })
    }

    private fun collect(){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toCollect().toHttpUrlOrNull()!!.newBuilder()
        var formBody = FormBody.Builder()
        formBody.add("id",currentNote.id.toString())
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        request.post(formBody.build())
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                miniLoadingDialog.dismiss()
                Toast.makeText(this@ShowActivity,"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@ShowActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 3
                handler.sendMessage(mess)
            }
        })
    }

    private fun comment(){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toComment().toHttpUrlOrNull()!!.newBuilder()
        var mediaType = "application/json".toMediaTypeOrNull()
        var json = JSONObject()
//        newNote = gson.fromJson(gson.toJson(currentNote),NoteInfo::class.java)
        var data = "{\n" +
                "    \"content\":\"${comment_content.text}\",\n" +
                "    \"note\":{\n" +
                "        \"id\": ${currentNote.id}\n" +
                "    }\n" +
                "}"
        var requestBody = RequestBody.create(mediaType,data)
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        request.post(requestBody)
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(this@ShowActivity,"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(this@ShowActivity,"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 4
                handler.sendMessage(mess)
            }
        })
    }

    private fun initTool() {
        SoftKeyBoardListener.setListener(this,object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                comment_option.visibility = View.GONE
                publish_comment.visibility = View.VISIBLE
//                Toast.makeText(this@EditActivity, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }
            override fun keyBoardHide(height: Int) {
                comment_option.visibility = View.VISIBLE
                publish_comment.visibility = View.GONE
//                Toast.makeText(this@EditActivity, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun initNoteTag(){
        var floTagLayout = FlowTagLayout(this)
        floTagLayout.adapter = ShowTagAdapter(this)
        floTagLayout.setSingleCancelable(true)
        floTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE)
        floTagLayout.addTags(currentNote.tags)
        current_tags.addView(floTagLayout)
    }

    private fun initExListView() {
        adapter = CommentExAdapter(list,this)
        common_ex_list.setAdapter(adapter)
        Utility.setListViewHeightBasedOnChildren(common_ex_list)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        //初始化信息开始
        markwon = Markwon.builder(this)
                .usePlugin(SoftBreakAddsNewLinePlugin())
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureTheme(builder: MarkwonTheme.Builder) {
                        builder.headingBreakHeight(1)
                                .bulletWidth(resources.getDimension(R.dimen.dp_6).toInt())
                    }
                }).build()
        current_writer.text = currentNote.user?.nickName
        current_writer_header.isCircle = true
        current_note_title.text = currentNote.title
        var data = SimpleDateFormat("YYYY-MM-dd")
        current_note_time.text = data.format(currentNote.createTime)
        markwon.setMarkdown(current_note_content, currentNote.content.toString())
        comment_num.text = "评论 ${currentNote.commentNum}条"
        current_zan_num.text = currentNote.like.toString()
        current_save_num.text = currentNote.collect.toString()
        IF_FOLLOW = currentNote.user?.follow == true
        IF_LIKE = currentNote.liked
        IF_COLLECT = currentNote.collected
        list = currentNote.comments!!
        changeStatus()
        initNoteTag()
        initTool()
        initExListView()
        //初始化信息结束
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeStatus(){
        if (IF_FOLLOW){
            follow_this_writer.background = resources.getDrawable(R.drawable.radio_button_unselect,null)
            follow_this_writer.text = "已关注"
            follow_this_writer.setTextColor(resources.getColor(R.color.little_gray,null))
        }else{
            follow_this_writer.background = resources.getDrawable(R.drawable.radio_button_select,null)
            follow_this_writer.text = "关 注"
            follow_this_writer.setTextColor(resources.getColor(R.color.white,null))
        }
        if(IF_LIKE) {
            ico_dianzan.setColorFilter(resources.getColor(R.color.orange, null))
        }else {
            ico_dianzan.setColorFilter(resources.getColor(R.color.little_gray, null))
        }
        if(IF_COLLECT) {
            ico_save.setColorFilter(resources.getColor(R.color.orange, null))
        }else{
            ico_save.setColorFilter(resources.getColor(R.color.little_gray,null))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.follow_this_writer -> {
                if (StateUtil.checkLoginStatus())
                    follow()
                else
                    Toast.makeText(this@ShowActivity,"你还未登陆哦", Toast.LENGTH_SHORT).show()
            }
            R.id.ico_dianzan -> {
                if (StateUtil.checkLoginStatus())
                    like()
                else
                    Toast.makeText(this@ShowActivity,"你还未登陆哦", Toast.LENGTH_SHORT).show()
            }
            R.id.ico_save -> {
                if (StateUtil.checkLoginStatus())
                    collect()
                else
                    Toast.makeText(this@ShowActivity,"你还未登陆哦", Toast.LENGTH_SHORT).show()
            }
            R.id.publish_comment -> {
                if (StateUtil.checkLoginStatus()) {
                    newComment = Comment()
                    newComment.likeNum = 0
                    var child: MutableList<Comment> = ArrayList()
                    newComment.child = child
                    newComment.content = comment_content.text.toString()
                    comment()
                }else
                    Toast.makeText(this@ShowActivity,"你还未登陆哦", Toast.LENGTH_SHORT).show()
            }
        }
    }
}