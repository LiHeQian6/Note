package com.example.note_android.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.xuexiang.xui.widget.imageview.RadiusImageView

class OptionNoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var head:RadiusImageView = itemView.findViewById(R.id.notice_option_head)
    var name:TextView = itemView.findViewById(R.id.notice_message_name)
    var time:TextView = itemView.findViewById(R.id.notice_message_time)
    var noteTitle:TextView = itemView.findViewById(R.id.notice_note_title)
}