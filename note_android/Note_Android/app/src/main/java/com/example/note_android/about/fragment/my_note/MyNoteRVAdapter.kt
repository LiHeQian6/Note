package com.example.note_android.about.fragment.my_note

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.example.note_android.listener.ViewListener
import java.util.zip.Inflater

class MyNoteRVAdapter(var list: MutableList<Int>, var recyclerView: RecyclerView) :
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

    }
}