package com.example.note_android.util

import android.content.Context
import android.content.SharedPreferences
import com.example.note_android.R
import com.google.gson.Gson
import com.tencent.tauth.Tencent
import okhttp3.OkHttpClient

class HttpAddressUtil {
    companion object{
        private const val IP = "http://47.94.247.44:8080/"
        private const val GET_VERIFY_CODE = "getMailCode/"

        fun getVerifyCodeIP(): String{
            return this.IP+this.GET_VERIFY_CODE
        }
    }
}