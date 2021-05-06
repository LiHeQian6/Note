package com.example.note_android.fragment.notice.type.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.Notice
import com.example.note_android.bean.NoticeInfo
import com.example.note_android.bean.SystemNotice
import com.example.note_android.listener.OnItemClickListener
import com.google.gson.Gson
import com.xuexiang.xui.widget.imageview.RadiusImageView
import java.text.SimpleDateFormat


class SystemNoticeAdapter(var list: List<Notice>,
                          var context: Context,
                          var recyclerView: RecyclerView):
    RecyclerView.Adapter<SystemNoticeAdapter.ViewHolder>(),View.OnClickListener {

    private var itemClickListener: OnItemClickListener? = null
    private var gson:Gson = Gson()

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.notice_item_layout, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.isCircle = true
        var noticeInfo = gson.fromJson(list[position].content,SystemNotice::class.java)
        holder.messageTitle.text = noticeInfo.userNickName
        holder.messageCont.text = noticeInfo.content
        var data = SimpleDateFormat("YYYY-MM-dd")
        holder.messageTime.text = data.format(list[position].createTime)
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: RadiusImageView = view.findViewById(R.id.message_image)
        var messageTitle: TextView = view.findViewById(R.id.message_name)
        var messageCont: TextView = view.findViewById(R.id.message_con)
        var messageTime: TextView = view.findViewById(R.id.message_time)
    }
}