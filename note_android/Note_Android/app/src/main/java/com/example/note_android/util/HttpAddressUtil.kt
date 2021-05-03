package com.example.note_android.util

import android.content.Context
import android.content.SharedPreferences
import com.example.note_android.R
import com.google.gson.Gson
import com.tencent.tauth.Tencent
import okhttp3.OkHttpClient

class HttpAddressUtil {
    companion object{
        //服务器IP
        private const val IP = "http://192.168.2.160:8080/"
        //获取邮件验证码
        private const val GET_VERIFY_CODE = "getMailCode"
        //注册
        private const val REGISTER = "register"
        //登陆
        private const val LOGIN = "login"
        //获取图片验证码
        private const val GET_IMAGE_CODE = "getImageCode"
        //注销
        private const val LOGOUT = "logout"
        //忘记密码
        private const val FORGET_PASSWORD = "forgotPassword"
        //笔记相关
        private const val PUBLISH_NOTE = "note"
        //获取笔记类别
        private const val TYPES = "types"
        //获取笔记标签
        private const val TAGS = "tags"
        //获取用户信息
        private const val USER_INFO = "getInfo"
        //浏览笔记信息
        private const val NOTE_INFO = "notes/popularity"
        //关注
        private const val FOLLOW = "follow"
        //点赞
        private const val LIKE = "like"
        //收藏
        private const val COLLECT = "collect"
        //获取用户关注作者笔记
        private const val FOLLOWER_NOTE = "notes/followers"
        //获取用户笔记
        private const val USER_NOTE = "notes/user"
        //修改用户信息
        private const val CHANGE_USER_INFO = "changeInfo"

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

        fun note(): String{
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

        fun getNoteInfo(): String{
            return this.IP+this.NOTE_INFO
        }

        fun toFollow(): String{
            return this.IP+this.FOLLOW
        }

        fun toLike(): String{
            return this.IP+this.LIKE
        }

        fun toCollect(): String{
            return this.IP+this.COLLECT
        }

        fun getFollowerNote(): String{
            return this.IP+this.FOLLOWER_NOTE
        }

        fun getUserNote(): String{
            return this.IP+this.USER_NOTE
        }

        fun getChangeInfo(): String{
            return this.IP+this.CHANGE_USER_INFO
        }
    }
}