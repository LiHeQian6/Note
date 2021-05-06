package com.example.note_android.holder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.xuexiang.xui.widget.imageview.RadiusImageView

class LikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var writerHeader: RadiusImageView = itemView.findViewById(R.id.like_head)
    var writeName: TextView = itemView.findViewById(R.id.like_nick_name)
    var writerIntroduce: TextView = itemView.findViewById(R.id.like_produce)
    var ifFollow: Button = itemView.findViewById(R.id.like_this_writer)
}