package com.example.note_android.about.fragment.my_note

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
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.edit.EditActivity
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
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet.BottomGridSheetBuilder
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView
import kotlinx.android.synthetic.main.fragment_follow.view.*
import kotlinx.android.synthetic.main.my_note_fragment.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class MyNoteFragment : Fragment() {

    companion object {
        fun newInstance() =
            MyNoteFragment()
    }

    private lateinit var viewModel: MyNoteViewModel
    private var list:MutableList<NoteInfo> = ArrayList()
    private lateinit var newList:MutableList<NoteInfo>
    private lateinit var myNoteRVAdapter: MyNoteRVAdapter
    private lateinit var handler: Handler
    private lateinit var adapter: FollowRVAdapter
    private val NOTE_SIZE = 10
    private var currentPage = 0
    private val gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNoteViewModel::class.java)
        // TODO: Use the ViewModel
        initRV()
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
                                my_note_refresh.finishLoadMore()
                                my_note_refresh.finishRefresh()
                                return
                            }
                            if (currentPage == 0) {
                                list.clear()
                                my_note_refresh.finishRefresh()
                            }
                            list.addAll(newList)
                            my_note_refresh.finishLoadMore()
                            myNoteRVAdapter.notifyDataSetChanged()
                        }
                        1 -> {
                            myNoteRVAdapter.removeItem(msg.arg1)
                        }
                    }
                }else{
                    my_note_refresh.finishRefresh()
                    Toast.makeText(requireContext(), "请重新登陆！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRefreshLayout() {
        my_note_refresh.setEnableLoadMore(true)
        my_note_refresh.autoRefresh()
        my_note_refresh.setOnRefreshListener {
            currentPage = 0
            initData()
        }
        my_note_refresh.setOnLoadMoreListener() {
            currentPage++
            initData()
            if (newList.size == 0)
                Toast.makeText(requireContext(), "已经到最后了哦", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData(){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getUserNote().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("page", currentPage.toString())
        urlBuilder.addQueryParameter("size", NOTE_SIZE.toString())
        urlBuilder.addPathSegment(StateUtil.SYSTEM_USER_INFO?.id.toString())
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                my_note_refresh.finishLoadMore()
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

    private fun deleteNote(position: Int){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.note().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addPathSegment(list[position].id.toString())
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .delete()
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
                mess.arg1 = position
                mess.what = 1
                handler.sendMessage(mess)
            }
        })
    }

    private fun initRV(){
        var layoutManager = LinearLayoutManager(requireContext())
        myNoteRVAdapter = MyNoteRVAdapter(list,my_note_recycler_view)
        var divider = DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider))
        my_note_recycler_view.addItemDecoration(divider)
        my_note_recycler_view.layoutManager = layoutManager
        my_note_recycler_view.adapter = myNoteRVAdapter
        myNoteRVAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                ActivityUtil.get().goActivityResult(requireActivity(), ShowActivity::class.java, list[position], SystemCode.READ_NOTE)
            }
        })
        myNoteRVAdapter.setLongClickListener(object : OnItemLongClickListener{
            override fun onLongClick(position: Int) {
                showBottomList(position)
            }
        })
    }

    private fun showBottomList(itemPosition: Int){
        BottomSheet.BottomListSheetBuilder(requireActivity())
            .addItem("删除")
            .addItem("分享")
            .addItem("编辑")
            .addItem("关闭")
            .setIsCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when(position){
                    0 -> {
                        dialog.dismiss()
                        deleteNote(itemPosition)
                    }
                    1 -> {
                        dialog.dismiss()
                        showBottomGrid()
                    }
                    2 -> {
                        dialog.dismiss()
                        ActivityUtil.get().goActivityResult(requireActivity(), EditActivity::class.java, list[itemPosition],SystemCode.EDIT_NOTE)
                    }
                    3 -> {
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
        val builder = BottomGridSheetBuilder(activity)
        builder
            .addItem(
                R.drawable.ico_qq,
                "分享给好友",
                TAG_SHARE_QQ,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_qq_space,
                "分享到空间",
                TAG_SHARE_QQ_SPACE,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat,
                "分享到微信",
                TAG_SHARE_WECHAT,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat_space,
                "分享到朋友圈",
                TAG_SHARE_WECHAT_SPACE,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .setOnSheetItemClickListener { dialog: BottomSheet, itemView: BottomSheetItemView ->
                dialog.dismiss()
                Toast.makeText(requireContext(),"功能正在开发",Toast.LENGTH_SHORT).show()
            }.build().show()
    }
}