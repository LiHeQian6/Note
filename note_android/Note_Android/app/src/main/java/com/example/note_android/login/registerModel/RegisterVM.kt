package com.example.note_android.login.registerModel

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.core.net.toUri
import com.example.note_android.listener.HttpListener
import com.example.note_android.login.CountDownTimer
import com.example.note_android.util.HttpAddressUtil
import com.google.gson.annotations.JsonAdapter
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
                var result = JSONObject(msg.obj.toString())
                when(msg.what){
                    0 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS"){
                            CountDownTimer(activity.get_verify_code).run()
                            Toast.makeText(activity.applicationContext,"发送成功，请查收！",Toast.LENGTH_SHORT).show()
                        }else
                            Toast.makeText(activity.applicationContext,"发送失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS") {
                            httpListener.complete("register", "Success")
                        }else
                            Toast.makeText(activity.applicationContext,"注册失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun getVerifyCode(email: String){
        var httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getVerifyCodeIP().toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("mail",email)
        urlBuilder?.addPathSegment("注册")
        var request = Request.Builder().url(urlBuilder?.build()!!).get().build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(activity.applicationContext,"网络出了点问题",Toast.LENGTH_SHORT).show()
                Looper.loop()
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
                handler.sendMessage(mess)
            }

        })
    }

    fun register(email:String,password:String,verifyCode:String){
        var httpClient = OkHttpClient()
        var json = JSONObject()
        json.put("username", email)
        json.put("password", password)
        var requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(),json.toString())
        val urlBuilder = HttpAddressUtil.getRegisterIP().toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addPathSegment(verifyCode)
        var request = Request.Builder().url(urlBuilder.build()).post(requestBody).build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(activity.applicationContext,"网络出了点问题",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(activity.applicationContext,"数据出了点问题,请重试",Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                handler.sendMessage(mess)
            }

        })
    }

    fun setHttpListener(httpListener: HttpListener){
        this.httpListener = httpListener
    }
}