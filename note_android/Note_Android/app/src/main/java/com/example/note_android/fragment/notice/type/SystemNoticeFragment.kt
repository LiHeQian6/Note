package com.example.note_android.fragment.notice.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.fragment.notice.Notice
import com.example.note_android.fragment.notice.type.adapter.SystemNoticeAdapter
import kotlinx.android.synthetic.main.system_notice_fragment.*

class SystemNoticeFragment : Fragment() {

    private var list: MutableList<Notice> = ArrayList()
    private lateinit var adapterSystem: SystemNoticeAdapter

    companion object {
        fun newInstance() = SystemNoticeFragment()
    }

    private lateinit var noticeViewModel: SystemNoticeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.system_notice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noticeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SystemNoticeViewModel::class.java)
        // TODO: Use the ViewModel
        initData()
        initRVAdapter()
    }

    private fun initData() {
        for (i in 1..15){
            list.add(Notice("李和谦","你好，大佬","2021-2-28"))
        }
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapterSystem = SystemNoticeAdapter(list,requireContext(),recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapterSystem
    }

    private fun initListener() {
//        adapter.setOnItemClickListener(object :
//            OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                XToast.success(requireContext(),"这是第${position+1}个").show()
//            }
//        })
    }
}