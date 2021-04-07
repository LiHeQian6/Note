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
import com.example.note_android.fragment.notice.type.adapter.OptionNoticeAdapter
import kotlinx.android.synthetic.main.like_notice_fragment.*
import kotlinx.android.synthetic.main.system_notice_fragment.*

class LikeNoticeFragment : Fragment() {

    companion object {
        fun newInstance() = LikeNoticeFragment()
    }

    private lateinit var noticeViewModel: LikeNoticeViewModel
    private lateinit var likeList:MutableList<Int>
    private lateinit var adapter:OptionNoticeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.like_notice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noticeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(LikeNoticeViewModel::class.java)
        initData()
        initList()
    }

    private fun initList() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = OptionNoticeAdapter(likeList,requireContext())
        like_recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        like_recyclerView.layoutManager = layoutManager
        like_recyclerView.adapter = adapter
    }

    private fun initData() {
        likeList = ArrayList()
        for (i in 1..10){
            likeList.add(i)
        }
    }

}