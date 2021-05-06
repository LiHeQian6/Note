package com.example.note_android.about.fragment.my_favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.holder.NoteViewHolder
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.example.note_android.listener.ViewListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class MyLikeRVAdapter(private var list: MutableList<Map<String,Object>>,
                      private var recyclerView: RecyclerView) :
    RecyclerView.Adapter<NoteViewHolder>(), ViewListener {

    private  var setLongClickListener: OnItemLongClickListener? = null
    private  var setClickListener: OnItemClickListener? = null
    private var gson = Gson()

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.setClickListener = itemClickListener
    }

    fun setLongClickListener(longClickListener: OnItemLongClickListener?) {
        this.setLongClickListener = longClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item,parent,false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.writerHeader.isCircle = true
        var noteInfo = gson.fromJson(gson.toJson(list[position]["note"]),NoteInfo::class.java)
        holder.writeName.text = noteInfo.user?.nickName
        holder.noteTitle.text = noteInfo.title
        var regex = Regex("[#->]")
        var content = noteInfo.content.toString().replace(regex,"")
        holder.noteLittleContent.text = content
        holder.viewNum.text = noteInfo.look.toString()
        holder.dianzanNum.text = noteInfo.like.toString()
        holder.commonNum.text = noteInfo.commentNum.toString()
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
}