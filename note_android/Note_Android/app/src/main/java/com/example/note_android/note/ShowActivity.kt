package com.example.note_android.note

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.bean.Note
import com.example.note_android.bean.NoteInfo
import com.example.note_android.bean.UserInfo
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_welcome.*
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
    private var commonList:MutableList<Common> = ArrayList()
    private lateinit var markwon: Markwon
    private lateinit var handler: Handler
    private var gson: Gson = Gson()
    private lateinit var miniLoadingDialog: MiniLoadingDialog
    private var IF_FOLLOW: Boolean = false

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
                    }
                }else{
                    Toast.makeText(this@ShowActivity,result.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initListener() {
        follow_this_writer.setOnClickListener(this)
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

    private fun initExListView() {
        adapter = CommentExAdapter(commonList,this)
        common_ex_list.setAdapter(adapter)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        //初始化信息开始
        markwon = Markwon.create(this)
        current_writer_header.isCircle = true
        current_note_title.text = currentNote.title
        var data = SimpleDateFormat("YYYY-MM-dd")
        current_note_time.text = data.format(currentNote.createTime)
        markwon.setMarkdown(current_note_content,currentNote.content.toString())
        comment_num.text = "评论 ${currentNote.commentNum}条"
        current_zan_num.text = currentNote.like.toString()
        current_save_num.text = currentNote.collect.toString()
        IF_FOLLOW = currentNote.user?.follow == true
        changeStatus()
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
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.follow_this_writer -> {
                if (StateUtil.checkLoginStatus())
                    follow()
                else
                    Toast.makeText(this@ShowActivity,"你还未登陆哦", Toast.LENGTH_SHORT).show()
            }
        }
    }
}