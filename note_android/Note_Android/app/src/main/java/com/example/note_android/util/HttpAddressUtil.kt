package com.example.note_android.util

import android.content.Context
import android.content.SharedPreferences
import com.example.note_android.R
import com.google.gson.Gson
import com.tencent.tauth.Tencent
import okhttp3.OkHttpClient

class HttpAddressUtil {
    companion object{
        private const val IP = "http://192.168.2.160:8080/"
        private const val GET_VERIFY_CODE = "getMailCode"
        private const val REGISTER = "register"
        private const val LOGIN = "login"
        private const val GET_IMAGE_CODE = "getImageCode"
        private const val LOGOUT = "logout"
        private const val FORGET_PASSWORD = "forgotPassword"
        private const val PUBLISH_NOTE = "note"
        private const val TYPES = "types"
        private const val TAGS = "tags"
        private const val USER_INFO = "getInfo"

        fun getVerifyCodeIP(): String{
            return this.IP+this.GET_VERIFY_CODE
        }

        fun getRegisterIP(): String{
            return this.IP+this.REGISTER
        }

        fun getImageCode(): String{
            return this.IP+this.GET_IMAGE_CODE
        }

        fun login(): String{
            return this.IP+this.LOGIN
        }

        fun logOut(): String{
            return this.IP+this.LOGOUT
        }

        fun resetPassword(): String{
            return this.IP+this.FORGET_PASSWORD
        }

        fun publishNote(): String{
            return this.IP+this.PUBLISH_NOTE
        }

        fun getTypes(): String{
            return this.IP+this.TYPES
        }

        fun getTags(): String{
            return this.IP+this.TAGS
        }

        fun getUserInfo(): String{
            return this.IP+this.USER_INFO
        }
    }
}