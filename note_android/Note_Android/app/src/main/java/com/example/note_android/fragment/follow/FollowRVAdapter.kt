package com.example.note_android.fragment.follow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.listener.RVItemOnClickListener

class FollowRVAdapter(var list: MutableList<Any>,
                      var context: Context,
                      var recyclerView: RecyclerView):
    RecyclerView.Adapter<FollowRVAdapter.ViewHolder>(),View.OnClickListener {

    private var onItemClickListener: RVItemOnClickListener? = null

    fun setOnItemClickListener(onItemClickListener: RVItemOnClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.follow_item_layout,parent,false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(position)
        }
    }
}