package com.example.note_android.about.fragment.my_note

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.example.note_android.listener.ViewListener
import com.xuexiang.xui.widget.imageview.RadiusImageView
import java.util.zip.Inflater

class MyNoteRVAdapter(var list: MutableList<NoteInfo>, var recyclerView: RecyclerView) :
        RecyclerView.Adapter<MyNoteRVAdapter.ViewHolder>(),
        ViewListener {

    private var setItemClickListener: OnItemClickListener? = null
    private var setLongClickListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.setItemClickListener = itemClickListener
    }

    fun setLongClickListener(longClickListener: OnItemLongClickListener?) {
        this.setLongClickListener = longClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.my_note_item, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text = list[position].title
        var regex = Regex("[#->]")
        var content = list[position].content.toString().replace(regex,"")
        holder.noteLittleContent.text = content
        holder.viewNum.text = list[position].look.toString()
        holder.dianzanNum.text = list[position].like.toString()
        holder.commonNum.text = "0"
    }

    override fun onLongClick(v: View?): Boolean {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (setLongClickListener != null) {
            setLongClickListener!!.onLongClick(position)
        }
        return true
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (setItemClickListener != null) {
            setItemClickListener!!.onItemClick(position)
        }
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView = itemView.findViewById(R.id.note_header)
        var noteLittleContent: TextView = itemView.findViewById(R.id.note_little_content)
        var viewNum: TextView = itemView.findViewById(R.id.view_num)
        var dianzanNum: TextView = itemView.findViewById(R.id.dianzan_num)
        var commonNum: TextView = itemView.findViewById(R.id.common_num)
    }
}