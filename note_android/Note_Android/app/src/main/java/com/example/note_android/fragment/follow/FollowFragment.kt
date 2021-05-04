package com.example.note_android.fragment.follow

import android.graphics.Typeface
import android.os.*
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.fragment.home.MainRVAdapter
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.note.ShowActivity
import com.example.note_android.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_follow.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class FollowFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel
    private lateinit var root: View
    private var list: MutableList<NoteInfo> = ArrayList()
    private lateinit var newList:MutableList<NoteInfo>
    private lateinit var handler: Handler
    private lateinit var adapter: FollowRVAdapter
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followViewModel =
            ViewModelProvider(this).get(FollowViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_follow, container, false)
        initRVAdapter()
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    val typeToken = object : TypeToken<MutableList<NoteInfo>?>() {}.type
                    newList = gson.fromJson(result.getJSONObject("data").get("content").toString(), typeToken)
                    if (newList.size == 0) {
                        root.follow_refresh.finishLoadMore()
                        root.follow_refresh.finishRefresh()
                        return
                    }
                    if (currentPage == 0) {
                        list.clear()
                        root.follow_refresh.finishRefresh()
                    }
                    list.addAll(newList)
                    root.follow_refresh.finishLoadMore()
                    adapter.notifyDataSetChanged()
                }else{
                    root.follow_refresh.finishRefresh()
                }
            }
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun fitSystem(){
//        var decorView = requireActivity().window.decorView;
//        var windowInsets = decorView.rootWindowInsets
//        if (windowInsets != null) {
//            root.dispatchApplyWindowInsets(windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), 0, 0))
//            root.setOnApplyWindowInsetsListener{v: View?, insets: WindowInsets? -> insets }
//        }
    }

    override fun onResume() {
        super.onResume()
        initRefreshLayout()
        StateBarUtils.setStatusBarLightMode(requireActivity())
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = FollowRVAdapter(list,requireContext(),root.follow_recyclerView)
        var divider = DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider,null))
        root.follow_recyclerView.addItemDecoration(divider)
        root.follow_recyclerView.layoutManager = layoutManager
        root.follow_recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                ActivityUtil.get().goActivityResult(requireActivity(), ShowActivity::class.java, list[position], SystemCode.READ_NOTE)
            }
        })
    }

    private fun initRefreshLayout() {
        root.follow_refresh.setEnableLoadMore(true)
        root.follow_refresh.autoRefresh()
        root.follow_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        root.follow_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getFollowerNote().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("page", currentPage.toString())
        urlBuilder.addQueryParameter("size", NOTE_SIZE.toString())
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                root.follow_refresh.finishRefresh()
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