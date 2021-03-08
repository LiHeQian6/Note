package com.example.note_android.about.fragment.my_like_up

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.example.note_android.listener.ViewListener

class MyClickUpAdapter(private var list: MutableList<Int>,
                       private var recyclerView: RecyclerView):
    RecyclerView.Adapter<MyClickUpAdapter.ViewHolder>(),
    ViewListener{

    private  var setLongClickListener: OnItemLongClickListener? = null
    private  var setClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.setClickListener = itemClickListener
    }

    fun setLongClickListener(longClickListener: OnItemLongClickListener?) {
        this.setLongClickListener = longClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.my_click_item,parent,false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (setClickListener != null) {
            setClickListener!!.onItemClick(position)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (setLongClickListener != null) {
            setLongClickListener!!.onLongClick(position)
        }
        return true
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}