package com.example.note_android.about.fragment.my_favourite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.about.fragment.my_like_up.MyClickUpAdapter
import com.example.note_android.about.fragment.my_note.MyNoteRVAdapter
import com.example.note_android.bean.NoteInfo
import com.example.note_android.fragment.follow.FollowRVAdapter
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.example.note_android.note.ShowActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.example.note_android.util.SystemCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView
import kotlinx.android.synthetic.main.my_click_up_fragment.*
import kotlinx.android.synthetic.main.my_like_fragment.*
import kotlinx.android.synthetic.main.my_note_fragment.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class MyLikeFragment : Fragment() {

    companion object {
        fun newInstance() = MyLikeFragment()
    }

    private lateinit var viewModel: MyLikeViewModel
    private lateinit var myLikeRVAdapter: MyLikeRVAdapter
    private var list:MutableList<Map<String,Object>> = ArrayList()
    private lateinit var newList:MutableList<Map<String,Object>>
    private lateinit var handler: Handler
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_like_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyLikeViewModel::class.java)
        initAdapter()
        initRefreshLayout()
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    when(msg.what){
                        0 -> {
                            val typeToken = object : TypeToken<MutableList<Map<String,Object>>?>() {}.type
                            newList = gson.fromJson(result.get("data").toString(), typeToken)
                            if (newList.size == 0) {
                                my_like_refresh.finishLoadMore()
                                my_like_refresh.finishRefresh()
                                return
                            }
                            if (currentPage == 0) {
                                list.clear()
                                my_like_refresh.finishRefresh()
                            }
                            list.addAll(newList)
                            my_like_refresh.finishLoadMore()
                            myLikeRVAdapter.notifyDataSetChanged()
                        }
                        1 -> {
                            myLikeRVAdapter.removeItem(msg.arg1)
                        }
                    }
                }else{
                    my_like_refresh.finishRefresh()
                    Toast.makeText(requireContext(), "请重新登陆！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRefreshLayout() {
        my_like_refresh.setEnableLoadMore(true)
        my_like_refresh.autoRefresh()
        my_like_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        my_like_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toCollect().toHttpUrlOrNull()!!.newBuilder()
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
                my_like_refresh.finishLoadMore()
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

    private fun collect(position: Int){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toCollect().toHttpUrlOrNull()!!.newBuilder()
        var formBody = FormBody.Builder()
        var noteInfo = gson.fromJson(gson.toJson(list[position]["note"]),NoteInfo::class.java)
        formBody.add("id", noteInfo.id.toString())
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization),StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header),StateUtil.AUTHORIZATION_HEADERS)
                .url(urlBuilder.build())
        request.post(formBody.build())
        httpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(),"服务器出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(requireContext(),"网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                mess.arg1 = position
                handler.sendMessage(mess)
            }
        })
    }

    private fun initAdapter() {
        myLikeRVAdapter = MyLikeRVAdapter(list,my_like_recycler_view)
        var layoutManager = LinearLayoutManager(requireContext())
        var divider = DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider))
        my_like_recycler_view.addItemDecoration(divider)
        my_like_recycler_view.layoutManager = layoutManager
        my_like_recycler_view.adapter = myLikeRVAdapter
        myLikeRVAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                var noteInfo = gson.fromJson(gson.toJson(list[position]["note"]),NoteInfo::class.java)
                ActivityUtil.get().goActivityResult(requireActivity(), ShowActivity::class.java, noteInfo, SystemCode.READ_NOTE)
            }
        })
        myLikeRVAdapter.setLongClickListener(object : OnItemLongClickListener {
            override fun onLongClick(position: Int) {
                showBottomList(position)
            }
        })
    }

    private fun showBottomList(itemPosition: Int){
        BottomSheet.BottomListSheetBuilder(activity)
            .addItem("删除")
            .addItem("分享")
            .addItem("关闭")
            .setIsCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when(position){
                    0 -> {
                        dialog.dismiss()
                        collect(itemPosition)
                    }
                    1 -> {
                        dialog.dismiss()
                        showBottomGrid()
                    }
                    2 -> {
                        dialog.dismiss()
                    }
                }
            }.build().show()
    }

    private fun showBottomGrid(){
        val TAG_SHARE_QQ = 0
        val TAG_SHARE_QQ_SPACE = 1
        val TAG_SHARE_WECHAT = 2
        val TAG_SHARE_WECHAT_SPACE = 3
        val builder = BottomSheet.BottomGridSheetBuilder(activity)
        builder
            .addItem(
                R.drawable.ico_qq,
                "分享给好友",
                TAG_SHARE_QQ,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_qq_space,
                "分享到空间",
                TAG_SHARE_QQ_SPACE,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat,
                "分享到微信",
                TAG_SHARE_WECHAT,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat_space,
                "分享到朋友圈",
                TAG_SHARE_WECHAT_SPACE,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .setOnSheetItemClickListener { dialog: BottomSheet, itemView: BottomSheetItemView ->
                dialog.dismiss()
                Toast.makeText(requireContext(),"功能正在开发", Toast.LENGTH_SHORT).show()
            }.build().show()
    }
}