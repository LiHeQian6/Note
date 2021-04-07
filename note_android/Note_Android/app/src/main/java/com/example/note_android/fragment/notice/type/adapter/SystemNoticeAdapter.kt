package com.example.note_android.fragment.notice.type.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.fragment.notice.Notice
import com.example.note_android.listener.OnItemClickListener
import com.xuexiang.xui.widget.imageview.RadiusImageView


class SystemNoticeAdapter(var list: List<Notice>,
                          var context: Context,
                          var recyclerView: RecyclerView):
    RecyclerView.Adapter<SystemNoticeAdapter.ViewHolder>(),View.OnClickListener {

    private var itemClickListener: OnItemClickListener? = null

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
        holder.messageTitle.text = list[position].title
        holder.messageCont.text = list[position].content
        holder.messageTime.text = list[position].time
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