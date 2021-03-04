package com.example.note_android.fragment.notice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.listener.RVItemOnClickListener
import com.xuexiang.xui.widget.imageview.RadiusImageView


class NoticeRVAdapter(var list: List<Notice>,
                      var context: Context,
                      var recyclerView: RecyclerView):
    RecyclerView.Adapter<NoticeRVAdapter.ViewHolder>(),View.OnClickListener {

    private var onItemClickListener: RVItemOnClickListener? = null

    fun setOnItemClickListener(onItemClickListener: RVItemOnClickListener?) {
        this.onItemClickListener = onItemClickListener
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
        holder.message_title.text = list[position].title
        holder.message_cont.text = list[position].content
        holder.message_time.text = list[position].time
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: RadiusImageView
        var message_title: TextView
        var message_cont: TextView
        var message_time: TextView

        init {
            image = view.findViewById(R.id.message_image)
            message_title = view.findViewById(R.id.message_name)
            message_cont = view.findViewById(R.id.message_con)
            message_time = view.findViewById(R.id.message_time)
        }
    }
}