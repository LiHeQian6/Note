package com.example.note_android.fragment.notice.type

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.bean.Notice
import com.example.note_android.bean.NoticeInfo
import com.example.note_android.fragment.notice.type.adapter.OptionNoticeAdapter
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.note.ShowActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.example.note_android.util.SystemCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.follow_notice_fragment.*
import kotlinx.android.synthetic.main.like_notice_fragment.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class FollowNoticeFragment : Fragment() {

    companion object {
        fun newInstance() = FollowNoticeFragment()
    }

    private var list:MutableList<Notice> = ArrayList()
    private lateinit var adapter:OptionNoticeAdapter
    private var newList:MutableList<Notice> = ArrayList()
    private lateinit var handler: Handler
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.follow_notice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRefreshLayout()
        initList()
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    val typeToken = object : TypeToken<MutableList<Notice>?>() {}.type
                    newList = gson.fromJson(result.getJSONObject("data").get("content").toString(), typeToken)
                    if (newList.size == 0) {
                        follow_notice_refresh.finishLoadMore()
                        follow_notice_refresh.finishRefresh()
                        return
                    }
                    if (currentPage == 0) {
                        list.clear()
                        follow_notice_refresh.finishRefresh()
                    }
                    list.addAll(newList)
                    follow_notice_refresh.finishLoadMore()
                    adapter.notifyDataSetChanged()
                }else{
                    follow_notice_refresh.finishRefresh()
                }
            }
        }
    }

    private fun initList() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = OptionNoticeAdapter(list,requireContext(),follow_notice_recyclerView)
        follow_notice_recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        follow_notice_recyclerView.layoutManager = layoutManager
        follow_notice_recyclerView.adapter = adapter
    }

    private fun initRefreshLayout() {
        follow_notice_refresh.setEnableLoadMore(true)
        follow_notice_refresh.autoRefresh()
        follow_notice_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        follow_notice_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getMessage().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("page", currentPage.toString())
        urlBuilder.addQueryParameter("size", NOTE_SIZE.toString())
        urlBuilder.addPathSegment("follow")
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                follow_notice_refresh.finishRefresh()
                Toast.makeText(requireContext(), "网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if (result == null || result == "") {
                    Toast.makeText(requireContext(), "网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                handler.sendMessage(mess)
            }
        })
    }
}