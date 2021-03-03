package com.example.note_android.login.registerModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData

class RegisterVM {

    private var context: Context

    constructor(context: Context){
        this.context = context
    }

    fun getVerifyCode(){
        Toast.makeText(context,"获取失败", Toast.LENGTH_SHORT).show()
    }

    fun register(email:String,password:String,verifyCode:String){
        //TODO 发起注册请求
    }
}