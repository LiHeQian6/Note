package com.example.note_android.edit.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.note_android.R
import com.example.note_android.bean.NoteType
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout
import kotlinx.android.synthetic.main.fragment_select_type.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException


class SelectTypeFragment() : Fragment(),View.OnClickListener{

    private lateinit var type: String
    private lateinit var url: String
    private var tags: MutableList<NoteType> = ArrayList()
    private lateinit var handler: Handler
    private var gson: Gson = Gson()
    private lateinit var data:MutableList<NoteType>
    private var currentSelect = -1

    constructor(type: String, url: String) : this(){
        this.type = type
        this.url = url
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    val typeToken = object : TypeToken<ArrayList<NoteType>?>() {}.type
                    if (type == "选择标签") {
                        data = gson.fromJson(result.getJSONObject("data").get("content").toString(), typeToken)
                        initNoteTag()
                    }else {
                        data = gson.fromJson(result.get("data").toString(), typeToken)
                        initNoteType()
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        publishNote()
    }

    private fun initView() {
        select_ok.setOnClickListener(this)
        select_title_text.text = type
    }

    private fun initNoteType(){
        for (item in 0 until data.size){
            var view = layoutInflater.inflate(R.layout.flow_tag_item, null) as LinearLayout
            view.findViewById<TextView>(R.id.type_parent).text = data[item].name
            var floTagLayout = FlowTagLayout(requireContext())
            floTagLayout.id = item
            floTagLayout.adapter = FlowTagAdapter(requireContext())
            floTagLayout.setSingleCancelable(true)
            floTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE)
            floTagLayout.addTags(data[item].child)
            floTagLayout.setOnTagSelectListener { parent, position, selectedList ->
                if(currentSelect != -1 && currentSelect != parent.id){
                    flow_tag_layout.findViewById<FlowTagLayout>(currentSelect).adapter.notifyDataSetChanged()
                }
                currentSelect = parent.id
                var noteType:NoteType = parent.adapter.getItem(position) as NoteType
                tags.add(0,noteType)
//                Toast.makeText(requireContext(),getSelectedText(parent,selectedList),Toast.LENGTH_SHORT).show()
            }
            view.addView(floTagLayout)
            flow_tag_layout.addView(view)
        }
    }

    private fun initNoteTag(){
        var view = layoutInflater.inflate(R.layout.flow_tag_item, null) as LinearLayout
        view.findViewById<TextView>(R.id.type_parent).text = "推荐标签"
        var floTagLayout = FlowTagLayout(requireContext())
        floTagLayout.adapter = FlowTagAdapter(requireContext())
        floTagLayout.setSingleCancelable(true)
        floTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI)
        floTagLayout.addTags(data)
        floTagLayout.setOnTagSelectListener { parent, position, selectedList ->
            currentSelect = parent.id
            var noteType:NoteType = parent.adapter.getItem(position) as NoteType
            if (tags.contains(noteType))
                tags.remove(noteType)
            else {
                if (tags.size == 5){
                    parent.setSelectedItems(tags)
                    Toast.makeText(requireContext(),"最多只能选择5个标签！",Toast.LENGTH_SHORT).show()
                    return@setOnTagSelectListener
                }
                tags.add(noteType)
            }
        }
        view.addView(floTagLayout)
        flow_tag_layout.addView(view)
    }

    private fun publishNote(){
        var httpClient = OkHttpClient()
        var formBody = FormBody.Builder()
        val urlBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        if(type == "选择标签") {
            urlBuilder.addQueryParameter("page", "0")
            urlBuilder.addQueryParameter("size", "99999")
        }
        var request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
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
                    Toast.makeText(requireContext(), "数据出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                handler.sendMessage(mess)
            }

        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.select_ok -> {
                var editNoteFragment: EditNoteFragment =
                        requireActivity().supportFragmentManager
                                .findFragmentByTag("editNoteFragment") as EditNoteFragment
                editNoteFragment.setData(type,tags)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}