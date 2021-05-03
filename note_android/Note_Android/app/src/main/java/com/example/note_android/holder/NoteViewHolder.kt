package com.example.note_android.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.xuexiang.xui.widget.imageview.RadiusImageView

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var writerHeader:RadiusImageView = itemView.findViewById(R.id.writer_header)
    var writeName:TextView = itemView.findViewById(R.id.writer_name)
    var noteTitle:TextView = itemView.findViewById(R.id.note_header)
    var noteLittleContent:TextView = itemView.findViewById(R.id.note_little_content)
    var viewNum:TextView = itemView.findViewById(R.id.view_num)
    var dianzanNum:TextView = itemView.findViewById(R.id.dianzan_num)
    var commonNum:TextView = itemView.findViewById(R.id.common_num)
}