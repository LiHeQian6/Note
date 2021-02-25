package com.example.note_android.util

import android.content.Context
import android.content.SharedPreferences
import com.example.note_android.R
import com.google.gson.Gson
import com.tencent.tauth.Tencent
import okhttp3.OkHttpClient

class Single {
    companion object{
        private var gson:Gson? = null
        private var sharedPreferences:SharedPreferences? = null

        fun getGson(): Gson? {
            if(gson == null){
                gson = Gson()
            }
            return gson
        }

        fun getShared(context: Context): SharedPreferences? {
            if(sharedPreferences == null){
                sharedPreferences = context.getSharedPreferences("QQLoginInfo",Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }
    }
}