package com.example.note_android.fragment.notice.type.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.holder.OptionNoticeViewHolder

class OptionNoticeAdapter(
        private var noticeList: MutableList<Int>,
        private var context: Context) : RecyclerView.Adapter<OptionNoticeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionNoticeViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.notice_items_layout,parent,false)
        return OptionNoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionNoticeViewHolder, position: Int) {
        holder.head.isCircle = true
        holder.name.text = "李和谦  赞了你的笔记"
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

}