package com.example.note_android.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.holder.NoteViewHolder
import com.example.note_android.listener.OnItemClickListener

class MainRVAdapter(var list: MutableList<Int>,
                    var context: Context,
                    var recyclerView: RecyclerView):
    RecyclerView.Adapter<NoteViewHolder>(), View.OnClickListener {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.home_item_layout,parent,false)
        view.setOnClickListener(this)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.writerHeader.isCircle = true
        holder.noteTitle.text = "Kotlin开发"
        holder.noteLittleContent.text = "浅谈Kotlin开发"
        holder.viewNum.text = "1"
        holder.saveNum.text = "2"
        holder.commonNum.text = "3"
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(position)
        }
    }
}