package com.example.note_android.login.registerModel

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.example.note_android.listener.HttpListener
import com.example.note_android.util.HttpAddressUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RegisterVM {

    private var activity: Activity
    private lateinit var handler: Handler
    private lateinit var httpListener: HttpListener


    constructor(activity: Activity){
        this.activity = activity
        initHandler()
    }

    private fun initHandler(){
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = msg.obj.toString() as JSONObject
                when(msg.what){
                    0 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS")
                            Toast.makeText(activity.applicationContext,"发送成功，请查收！",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(activity.applicationContext,"发送失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS")
                            httpListener.complete("register","Success")
                        else
                            Toast.makeText(activity.applicationContext,"注册失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun getVerifyCode(){
        var httpClient = OkHttpClient()
        var request = Request.Builder().url(HttpAddressUtil.getVerifyCodeIP()).get().build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(activity.applicationContext,"网络出了点问题",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(activity.applicationContext,"数据出了点问题,请重试",Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 0
                handler?.sendMessage(mess)
            }

        })
    }

    fun register(email:String,password:String,verifyCode:String){
        var httpClient = OkHttpClient()
        var resultBody = FormBody.Builder()
            .add("username",email)
            .add("password",password).build()
        var request = Request.Builder().url(HttpAddressUtil.getVerifyCodeIP()+verifyCode).post(resultBody).build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(activity.applicationContext,"网络出了点问题",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(activity.applicationContext,"数据出了点问题,请重试",Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 0
                handler?.sendMessage(mess)
            }

        })
    }

    fun setHttpListener(httpListener: HttpListener){
        this.httpListener = httpListener
    }
}