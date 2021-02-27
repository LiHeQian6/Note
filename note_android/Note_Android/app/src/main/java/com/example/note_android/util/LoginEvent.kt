package com.example.note_android.util

import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo
import org.json.JSONObject

class LoginEvent {

    var userInfo:QQUserInfo? = null
                get() = field
                set(value) {
                    field = value
                }

    var loginInfo:QQLoginInfo? = null
        get() = field
        set(value) {
            field = value
        }

    var p1:JSONObject = JSONObject()
        get() = field
        set(value) {
            field = value
        }

    constructor(loginInfo: QQLoginInfo,p1: JSONObject){
        this.loginInfo = loginInfo
        this.p1 = p1
    }

    constructor(userInfo: QQUserInfo){
        this.userInfo = userInfo
    }
}