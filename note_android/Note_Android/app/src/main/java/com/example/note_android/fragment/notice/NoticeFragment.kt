package com.example.note_android.fragment.notice

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.fragment.RVItemOnClickListener
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_notice.view.*

@Page(name = "消息页")
class NoticeFragment : Fragment(),View.OnClickListener {

    private lateinit var noticeViewModel: NoticeViewModel
    private lateinit var root: View
    private var list: MutableList<Notice> = ArrayList()
    private lateinit var adapter: NoticeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noticeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        StateBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        root = inflater.inflate(R.layout.fragment_notice, container, false)
        initToolBar()
        initData()
        initRVAdapter()
        initListener()
        return root
    }

    private fun initToolBar() {
        var title: TextView = root.notice_tool_bar.getChildAt(0) as TextView
        title.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        title.gravity = Gravity.CENTER_HORIZONTAL
        title.textSize = resources.getDimension(R.dimen.sp_6)
        title.typeface = Typeface.DEFAULT
    }

    private fun initData() {
        for (i in 1..20){
            list.add(Notice("李和谦","你好，大佬","2021-2-28"))
        }
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = NoticeRVAdapter(list,requireContext(),root.recyclerView)
        root.recyclerView.layoutManager = layoutManager
        root.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        root.recyclerView.adapter = adapter
    }

    private fun initListener() {
        adapter.setOnItemClickListener(object : RVItemOnClickListener{
            override fun onItemClick(position: Int) {
                XToast.success(requireContext(),"这是第${position+1}个").show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }


}