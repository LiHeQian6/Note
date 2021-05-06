package com.example.note_android.about.fragment.my_like

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.bean.NoteInfo
import com.example.note_android.bean.UserInfo
import com.example.note_android.holder.LikeViewHolder
import com.example.note_android.holder.NoteViewHolder
import com.example.note_android.listener.ViewListener
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.my_like_fragment.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class MyFollowAdapter(private var list: MutableList<Map<String,Object>>,
                      private var context: Context, ):
        RecyclerView.Adapter<LikeViewHolder>(){

    private var gson:Gson = Gson()
    private var handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    removeItem(msg.what)
                    Toast.makeText(context, "取消关注成功！", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "请重试！", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.like_item_layout,parent,false)
        return LikeViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        holder.writerHeader.isCircle = true
        var userInfo = gson.fromJson(gson.toJson(list[position]["user"]),UserInfo::class.java)
        holder.writeName.text = userInfo.nickName
        holder.writerIntroduce.text = userInfo.introduction
        holder.ifFollow.background = context.resources.getDrawable(R.drawable.radio_button_unselect,null)
        holder.ifFollow.text = "已关注"
        holder.ifFollow.setTextColor(context.resources.getColor(R.color.little_gray,null))
        holder.ifFollow.setOnClickListener { cancelFollow(userInfo,position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun cancelFollow(user:UserInfo,position: Int){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.toFollow().toHttpUrlOrNull()!!.newBuilder()
        var formBody = FormBody.Builder()
        formBody.add("targetId",user.id.toString())
        val request = Request.Builder()
                .addHeader(context.resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(context.resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .delete(formBody.build())
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(context, "网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if (result == null || result == "") {
                    Toast.makeText(context, "网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = position
                handler.sendMessage(mess)
            }
        })
    }
}