package com.example.note_android.fragment.notice.type.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.Notice
import com.example.note_android.bean.NoticeInfo
import com.example.note_android.holder.OptionNoticeViewHolder
import com.example.note_android.listener.OnItemClickListener
import com.google.gson.Gson
import java.text.SimpleDateFormat

class OptionNoticeAdapter(
        private var noticeList: MutableList<Notice>,
        private var context: Context,
        private var recyclerView: RecyclerView) : RecyclerView.Adapter<OptionNoticeViewHolder>(),View.OnClickListener {

    private var gson = Gson()
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionNoticeViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.notice_items_layout,parent,false)
        view.setOnClickListener(this)
        return OptionNoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionNoticeViewHolder, position: Int) {
        holder.head.isCircle = true
        var noticeInfo = gson.fromJson(noticeList[position].content,NoticeInfo::class.java)
        var option = ""
        when(noticeList[position].msType){
            "like" -> {
                option = "  赞了你的笔记"
                holder.content.visibility = View.GONE
                holder.noteTitle.text = noticeInfo.entityInfo
            }
            "comment" -> {
                option = "  评论了你的笔记"
                holder.content.visibility = View.VISIBLE
                holder.content.text = noticeInfo.commentContent
                holder.noteTitle.text = noticeInfo.entityInfo
            }
            "follow" -> {
                option = ""
                holder.content.visibility = View.VISIBLE
                holder.noteTitleLi.visibility = View.GONE
                holder.content.text = "我关注了你哦！"
            }
        }
        holder.name.text = noticeInfo.entityUserNickname + option
        var data = SimpleDateFormat("YYYY-MM-dd")
        holder.time.text = data.format(noticeList[position].createTime)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(position)
        }
    }
}