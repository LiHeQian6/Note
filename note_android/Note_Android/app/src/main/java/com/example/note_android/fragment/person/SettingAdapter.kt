package com.example.note_android.fragment.person

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.setting_item.view.*

class SettingAdapter(var itemArray: Array<String>,
                     var context: Context,
                     var layoutId:Int) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var view: View
        if(null == convertView){
            view = LayoutInflater.from(context).inflate(layoutId, null)
            viewHolder = ViewHolder()
            viewHolder.itemName = view.item_name
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.itemName?.text = itemArray.get(position)
        return view
    }

    override fun getItem(position: Int): Any {
        return itemArray.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemArray.size
    }

    private class ViewHolder{
        var itemName : TextView? = null
    }
}