package com.example.note_android.fragment.home

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.bean.NoteInfo
import com.example.note_android.edit.EditActivity
import com.example.note_android.fragment.notice.NoticeViewModel
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.note.ShowActivity
import com.example.note_android.scan.ScanActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateBarUtils
import com.example.note_android.util.SystemCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

@Page(name = "主页")
class HomeFragment : Fragment(),View.OnClickListener {

    private lateinit var homeViewModel: NoticeViewModel
    private lateinit var root: View
    private var list: MutableList<NoteInfo> = ArrayList()
    private lateinit var handler:Handler
    private lateinit var adapter:MainRVAdapter
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson:Gson = Gson()
    private lateinit var loadingDialog:MiniLoadingDialog

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        initListener()
        initRefreshLayout()
        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    val typeToken = object : TypeToken<MutableList<NoteInfo>?>() {}.type
                    var newList:MutableList<NoteInfo> = gson.fromJson(result.getJSONObject("data").get("content").toString(), typeToken)
                    if (newList.size == 0) {
                        Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
                        root.home_refresh.finishLoadMore()
                        return
                    }
                    if (currentPage == 0) {
                        list.clear()
                        root.home_refresh.finishRefresh()
                    }
                    list.addAll(newList)
                    root.home_refresh.finishLoadMore()
                    initRVAdapter()
                }
            }
        }
        return root
    }

    private fun initRefreshLayout() {
        root.home_refresh.setEnableLoadMore(true)
        root.home_refresh.autoRefresh()
        root.home_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        root.home_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.setStatusBarLightMode(requireActivity())
    }

    private fun initRVAdapter() {
        if(currentPage != 0){
            adapter.list = list
            adapter.notifyDataSetChanged()
            return
        }
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = MainRVAdapter(list, requireContext(), root.home_recyclerView)
        var divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider, null))
        root.home_recyclerView.layoutManager = layoutManager
        root.home_recyclerView.addItemDecoration(divider)
        root.home_recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :
                OnItemClickListener {
            override fun onItemClick(position: Int) {
                ActivityUtil.get().goActivityResult(requireActivity(), ShowActivity::class.java, list[position],SystemCode.READ_NOTE)
            }
        })
//        root.home_recyclerView.addOnItemTouchListener()
    }

    private fun initData() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getNoteInfo().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("page", currentPage.toString())
        urlBuilder.addQueryParameter("size", NOTE_SIZE.toString())
        val request = Request.Builder()
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
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

    private fun initListener() {
        root.add_button.setOnClickListener(this)
        root.saoma.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_button -> {
                ActivityUtil.get().activity(requireContext(), EditActivity::class.java)
            }
            R.id.saoma -> {
                ScanActivity.start(requireActivity(), 1, R.style.XQRCodeTheme_Custom)
            }
        }
    }
}