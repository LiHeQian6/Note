package com.example.note_android.login.bean

data class QQLoginInfo(var ret:String,
                       var pay_token:String,
                       var pf:String,
                       var expires_in:String,
                       var openid:String,
                       var pfkey:String,
                       var msg:String,
                       var access_token:String)