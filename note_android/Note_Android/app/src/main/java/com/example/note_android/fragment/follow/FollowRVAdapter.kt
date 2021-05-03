package com.example.note_android.fragment.follow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.holder.NoteViewHolder
import com.example.note_android.listener.OnItemClickListener

class FollowRVAdapter(var list: MutableList<NoteInfo>,
                      var context: Context,
                      var recyclerView: RecyclerView):
    RecyclerView.Adapter<NoteViewHolder>(),View.OnClickListener {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_layout,parent,false)
        view.setOnClickListener(this)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.writerHeader.isCircle = true
        holder.writeName.text = list[position].user?.nickName
        holder.noteTitle.text = list[position].title
        var regex = Regex("[#->]")
        var content = list[position].content.toString().replace(regex,"")
        holder.noteLittleContent.text = content
        holder.viewNum.text = list[position].look.toString()
        holder.dianzanNum.text = list[position].like.toString()
        holder.commonNum.text = "0"
    }

    override fun onClick(v: View?) {
        val position: Int = recyclerView.getChildAdapterPosition(v!!)
        if (itemClickListener != null) {
            itemClickListener!!.onItemClick(position)
        }
    }
}