package com.example.note_android.about.fragment.my_like_up

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
import kotlinx.android.synthetic.main.my_note_fragment.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class MyClickUpFragment : Fragment() {

    companion object {
        fun newInstance() = MyClickUpFragment()
    }

    private lateinit var clickUpAdapter: MyClickUpAdapter
    private var list:MutableList<NoteInfo> = ArrayList()
    private lateinit var newList:MutableList<NoteInfo>
    private lateinit var handler: Handler
    private lateinit var adapter: FollowRVAdapter
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_click_up_fragment, container, false)
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
                            val typeToken = object : TypeToken<MutableList<NoteInfo>?>() {}.type
                            newList = gson.fromJson(result.getJSONObject("data").get("content").toString(), typeToken)
                            if (newList.size == 0) {
                                my_click_refresh.finishLoadMore()
                                my_click_refresh.finishRefresh()
                                return
                            }
                            if (currentPage == 0) {
                                list.clear()
                                my_click_refresh.finishRefresh()
                            }
                            list.addAll(newList)
                            my_click_refresh.finishLoadMore()
                            clickUpAdapter.notifyDataSetChanged()
                        }
                    }
                }else{
                    my_click_refresh.finishRefresh()
                    Toast.makeText(requireContext(), "请重新登陆！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRefreshLayout() {
        my_click_refresh.setEnableLoadMore(true)
        my_click_refresh.autoRefresh()
        my_click_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        my_click_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData(){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toLike().toHttpUrlOrNull()!!.newBuilder()
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
                my_click_refresh.finishLoadMore()
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
        clickUpAdapter = MyClickUpAdapter(list,click_up_recycler_view)
        var layoutManager = LinearLayoutManager(requireContext())
        var divider = DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider))
        click_up_recycler_view.addItemDecoration(divider)
        click_up_recycler_view.layoutManager = layoutManager
        click_up_recycler_view.adapter = clickUpAdapter
        clickUpAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                ActivityUtil.get().goActivityResult(requireActivity(), ShowActivity::class.java, list[position], SystemCode.READ_NOTE)
            }
        })
        clickUpAdapter.setLongClickListener(object : OnItemLongClickListener {
            override fun onLongClick(position: Int) {
                showBottomList(position)
            }
        })
    }

    private fun showBottomList(itemPosition: Int){
        BottomSheet.BottomListSheetBuilder(activity)
            .addItem("分享")
            .addItem("关闭")
            .setIsCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when(position){
                    0 -> {
                        dialog.dismiss()
                        showBottomGrid()
                    }
                    1 -> {
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