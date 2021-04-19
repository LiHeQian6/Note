package com.example.note_android.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    private lateinit var showViewModel: ShowViewModel
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var commonList:MutableList<Common>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        showViewModel = ViewModelProvider(this).get(ShowViewModel::class.java)
        commonList = ArrayList()
        initView()
        initData()
        initExListView()
    }

    private fun initData() {
        for (i in 1..5){
            var list:MutableList<Reply> = ArrayList()
            for (j in 1..3){
                var rep = Reply("千寻一醉","一字见心","说的真好！","99")
                list.add(rep)
            }
            var com = Common("一字见心","赞！","100","100",list)
            commonList.add(com)
        }
    }

    private fun initExListView() {
        adapter = CommentExAdapter(commonList,this)
        common_ex_list.setAdapter(adapter)
    }

    private fun initView() {
        current_writer_header.isCircle = true
    }
}