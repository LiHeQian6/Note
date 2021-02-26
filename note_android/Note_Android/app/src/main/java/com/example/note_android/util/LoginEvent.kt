package com.example.note_android.util

import com.example.note_android.login.bean.QQUserInfo

class LoginEvent {

    var userInfo:QQUserInfo? = null
                get() = field
                set(value) {
                    field = value
                }

    constructor(userInfo: QQUserInfo){
        this.userInfo = userInfo
    }
}