package com.example.note_android.about.fragment.my_like

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_my_like.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException


class MyFollowFragment : Fragment() {

    companion object {
        fun newInstance() = MyFollowFragment()
    }

    private lateinit var myFollowRVAdapter: MyFollowAdapter
    private var list:MutableList<Map<String,Object>> = ArrayList()
    private lateinit var newList:MutableList<Map<String,Object>>
    private lateinit var handler: Handler
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_like, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initRefreshLayout()
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    when(msg.what){
                        0 -> {
                            val typeToken = object : TypeToken<MutableList<Map<String, Object>>?>() {}.type
                            newList = gson.fromJson(result.get("data").toString(), typeToken)
                            if (newList.size == 0) {
                                my_follow_refresh.finishLoadMore()
                                my_follow_refresh.finishRefresh()
                                return
                            }
                            if (currentPage == 0) {
                                list.clear()
                                my_follow_refresh.finishRefresh()
                            }
                            list.addAll(newList)
                            my_follow_refresh.finishLoadMore()
                            myFollowRVAdapter.notifyDataSetChanged()
                        }
                        1 -> {
                            myFollowRVAdapter.removeItem(msg.arg1)
                        }
                    }
                }else{
                    my_follow_refresh.finishRefresh()
                    Toast.makeText(requireContext(), "请重新登陆！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRefreshLayout() {
        my_follow_refresh.setEnableLoadMore(true)
        my_follow_refresh.autoRefresh()
        my_follow_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        my_follow_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getLikeMe().toHttpUrlOrNull()!!.newBuilder()
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
                my_follow_refresh.finishLoadMore()
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
                mess.what = 0
                handler.sendMessage(mess)
            }
        })
    }

    private fun initAdapter() {
        myFollowRVAdapter = MyFollowAdapter(list,requireContext())
        var layoutManager = LinearLayoutManager(requireContext())
        var divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider))
        my_follow_recycler_view.addItemDecoration(divider)
        my_follow_recycler_view.layoutManager = layoutManager
        my_follow_recycler_view.adapter = myFollowRVAdapter
    }
}