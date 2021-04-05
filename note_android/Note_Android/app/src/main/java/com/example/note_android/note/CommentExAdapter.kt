package com.example.note_android.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.note_android.R
import com.xuexiang.xui.widget.imageview.RadiusImageView

class CommentExAdapter(
        private var commentList:MutableList<Common>,
        private var context:Context) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return commentList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return commentList[groupPosition].child.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return commentList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return commentList[groupPosition].child[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        val commentVH:CommentViewHolder
        var view = convertView
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.comment_list_item,parent,false)
            commentVH = CommentViewHolder()
            commentVH.header = view.findViewById(R.id.commoner_header)
            commentVH.name = view.findViewById(R.id.commoner_name)
            commentVH.content = view.findViewById(R.id.commoner_content)
            commentVH.zan = view.findViewById(R.id.commoner_zan_num)
            view.tag = commentVH
        }else{
            commentVH = view?.tag as CommentViewHolder
        }
        commentVH.header.isCircle = true
        commentVH.name.text = commentList[groupPosition].name
        commentVH.content.text = commentList[groupPosition].content
        commentVH.zan.text = commentList[groupPosition].zan
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        val replyVH:ReplyViewHolder
        var view = convertView
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.reply_list_item,parent,false)
            replyVH = ReplyViewHolder()
            replyVH.header = view.findViewById(R.id.reply_header)
            replyVH.name = view.findViewById(R.id.reply_name)
            replyVH.content = view.findViewById(R.id.reply_content)
            replyVH.zan = view.findViewById(R.id.reply_zan_num)
            view.tag = replyVH
        }else{
            replyVH = view?.tag as ReplyViewHolder
        }
        var reply = commentList[groupPosition].child[childPosition]
        replyVH.header.isCircle = true
        replyVH.name.text = "${reply.fromName}"
        replyVH.content.text = "@${reply.toName} "+reply.content
        replyVH.zan.text = reply.zan
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private inner class CommentViewHolder{
        lateinit var header:RadiusImageView
        lateinit var name:TextView
        lateinit var content:TextView
        lateinit var zan:TextView
    }

    private inner class ReplyViewHolder{
        lateinit var header:RadiusImageView
        lateinit var name:TextView
        lateinit var content:TextView
        lateinit var zan:TextView
    }
}