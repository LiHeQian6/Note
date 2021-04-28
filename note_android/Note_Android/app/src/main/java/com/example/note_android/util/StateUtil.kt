package com.example.note_android.util

import android.content.Context
import com.example.note_android.R
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo
import com.google.gson.Gson

class StateUtil {
    companion object{
        var EDITOR = SystemCode.MARKDOWN
        var IF_LOGIN:Boolean = false
        var USER_INFO: QQUserInfo? = null
        var LOGIN_INFO: QQLoginInfo? = null
        var LOGIN_TYPE:String = ""
        var AUTHORIZATION:String = ""
        var AUTHORIZATION_HEADERS:String = ""

        fun initInfo(context: Context): Boolean{
            var gson = Gson()
            var editor = context.getSharedPreferences(
                context.resources.getString(R.string.LoginInfo),
                Context.MODE_PRIVATE)
            var userInfo = editor?.getString(context.resources.getString(R.string.QQUserInfo),"")
            var loginInfo = editor?.getString(context.resources.getString(R.string.QQLoginInfo),"")
            if (userInfo.equals("") || loginInfo.equals("")){
                return false
            }
            this.LOGIN_INFO = gson.fromJson(loginInfo, QQLoginInfo::class.java)
            this.USER_INFO = gson.fromJson(userInfo, QQUserInfo::class.java)
            return true
        }
    }
}