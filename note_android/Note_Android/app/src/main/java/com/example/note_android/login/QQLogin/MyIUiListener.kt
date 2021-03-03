package com.example.note_android.login.QQLogin

import android.content.Context
import com.example.note_android.R
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo
import com.example.note_android.util.LoginEvent
import com.example.note_android.util.Single
import com.example.note_android.util.StateUtil
import com.example.note_android.util.UserEvent
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject


class MyIUiListener(var context: Context,var option: String) : IUiListener {

    override fun onComplete(p0: Any?) {
        when(option){
            "login" -> {
                login(p0 as JSONObject)
            }
            "get_user_info" -> {
                getUserInfo(p0 as JSONObject)
            }
        }
    }

    private fun login(p1: JSONObject){
        var loginInfo = Single.getGson()?.fromJson(p1.toString(), QQLoginInfo::class.java)
        if (loginInfo != null) {
            StateUtil.LOGIN_INFO = loginInfo
            var shared = Single.getShared(context)?.edit()
            shared?.putString(context.resources.getString(R.string.Login_Type),context.resources.getString(R.string.LoginWithQQ))
            shared?.putString(context.resources.getString(R.string.QQLoginInfo),p1.toString())
            shared?.apply()
            StateUtil.IF_LOGIN = true
            EventBus.getDefault().post(LoginEvent(StateUtil.LOGIN_INFO!!,p1))
        }else{
            XToast.error(context,"登陆失败").show()
        }
    }

    private fun getUserInfo(p1: JSONObject){
        StateUtil.USER_INFO = Single.getGson()?.fromJson(p1.toString(), QQUserInfo::class.java)
        if(StateUtil.USER_INFO != null){
            var shared = Single.getShared(context)?.edit()
            shared?.putString(context.resources.getString(R.string.QQUserInfo),p1.toString())
            shared?.apply()
            EventBus.getDefault().post(UserEvent(StateUtil.USER_INFO!!))
        }else {
            XToast.error(context,"用户信息获取失败").show()
        }
    }

    override fun onCancel() {
    }

    override fun onWarning(p0: Int) {
    }

    override fun onError(p0: UiError?) {
    }

}