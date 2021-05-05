package com.example.note_android.note

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.note_android.R
import com.example.note_android.bean.NoteType
import com.example.note_android.bean.Tag
import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter

class ShowTagAdapter(context: Context) : BaseTagAdapter<Tag, TextView>(context) {

    override fun newViewHolder(convertView: View?): TextView {
        return (convertView!!.findViewById<View>(R.id.tv_tag) as TextView)
    }

    override fun getLayoutId(): Int {
        return R.layout.show_item_tag
    }

    override fun convert(holder: TextView?, item: Tag?, position: Int) {
        holder?.text = item?.name
    }
}