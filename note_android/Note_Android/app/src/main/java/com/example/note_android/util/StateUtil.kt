package com.example.note_android.util

import android.content.Context
import android.content.SharedPreferences
import com.example.note_android.R
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo

class StateUtil {
    companion object{
        var EDITOR = SystemCodeUtil.MARKDOWN
        var IF_LOGIN:Boolean = false
        var USER_INFO: QQUserInfo? = null
        var LOGIN_INFO: QQLoginInfo? = null
        var LOGIN_TYPE:String = ""

        fun initInfo(context: Context){
            var editor = Single.getShared(context)
            var userInfo = editor?.getString(context.resources.getString(R.string.QQUserInfo),"")
            var loginInfo = editor?.getString(context.resources.getString(R.string.QQLoginInfo),"")
            this.LOGIN_INFO = Single.getGson()?.fromJson(loginInfo, QQLoginInfo::class.java)
            this.USER_INFO = Single.getGson()?.fromJson(userInfo, QQUserInfo::class.java)
        }
    }
}