package com.example.note_android.util

import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo

class StateUtil {
    companion object{
        var EDITOR = SystemCodeUtil.MARKDOWN
        var IF_LOGIN:Boolean = false
        var USER_INFO: QQUserInfo? = null
        var LOGIN_INFO: QQLoginInfo? = null
        var LOGIN_TYPE:String = ""
    }
}