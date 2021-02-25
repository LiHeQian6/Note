package com.example.note_android.login.QQLogin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.note_android.MainActivity
import com.example.note_android.R
import com.example.note_android.login.bean.QQLoginInfo
import com.example.note_android.login.bean.QQUserInfo
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.sql_lite.QQLoginDbSchema
import com.example.note_android.sql_lite.QQUserDbSchema
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.Single
import com.example.note_android.util.StateUtil
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast


class MyIUiListener(var context: Context,var option: String) : IUiListener {

    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onComplete(p0: Any?) {
        dataBaseHelper = DataBaseHelper(context,1)
        sqLiteDatabase = dataBaseHelper.writableDatabase
        when(option){
            "login" -> {
                login(p0)
            }
            "get_user_info" -> {
                getUserInfo(p0)
            }
        }
    }

    private fun login(p0: Any?){
        var loginInfo = Single.getGson()?.fromJson(p0.toString(),QQLoginInfo::class.java)
        StateUtil.LOGIN_INFO = loginInfo
        var shared = Single.getShared(context)?.edit()
        shared?.putString(context.resources.getString(R.string.Login_Type),context.resources.getString(R.string.LoginWithQQ))
        shared?.commit()
        if (loginInfo != null) {
            insertLoginData(context,loginInfo)
        }else{
            XToast.error(context,"登陆失败").show()
        }
        StateUtil.IF_LOGIN = true
//        mTencent?.openId = StateUtil.LOGIN_INFO?.openid
//        mTencent?.setAccessToken(StateUtil.LOGIN_INFO?.access_token,StateUtil.LOGIN_INFO?.expires_in)
//        var user = UserInfo(context, mTencent?.qqToken)
//        user.getUserInfo(MyIUiListener(context,"get_user_info"))
        ActivityUtil.get()?.goActivityKill(context, MainActivity::class.java)
    }

    private fun getUserInfo(p0: Any?){
        StateUtil.USER_INFO = Single.getGson()?.fromJson(p0.toString(),QQUserInfo::class.java)
//        context.
        if(StateUtil.USER_INFO != null){
            insertUserData(context, StateUtil.USER_INFO!!)
        }else {
            XToast.error(context,"用户信息获取失败").show()
        }
    }

    private fun insertUserData(context: Context,qqUserInfo: QQUserInfo){
        val cv = ContentValues()
        cv.put(QQUserDbSchema.IS_YELLOW_YEAR_VIP, qqUserInfo.is_yellow_year_vip)
        cv.put(QQUserDbSchema.RET, qqUserInfo.ret)
        cv.put(QQUserDbSchema.FIGUREURL_QQ_1, qqUserInfo.figureurl_qq_1)
        cv.put(QQUserDbSchema.FIGUREURL_QQ_2, qqUserInfo.figureurl_qq_2)
        cv.put(QQUserDbSchema.NICKNAME, qqUserInfo.nickname)
        cv.put(QQUserDbSchema.YELLOW_VIP_LEVEL, qqUserInfo.yellow_vip_level)
        cv.put(QQUserDbSchema.MSG, qqUserInfo.msg)
        cv.put(QQUserDbSchema.FIGUREURL_1, qqUserInfo.figureurl_1)
        cv.put(QQUserDbSchema.VIP, qqUserInfo.vip)
        cv.put(QQUserDbSchema.LEVEL, qqUserInfo.level)
        cv.put(QQUserDbSchema.FIGUREURL_2, qqUserInfo.figureurl_2)
        cv.put(QQUserDbSchema.IS_YELLOW_VIP, qqUserInfo.is_yellow_vip)
        cv.put(QQUserDbSchema.GENDER, qqUserInfo.gender)
        cv.put(QQUserDbSchema.FIGUREURL, qqUserInfo.figureurl)
        sqLiteDatabase.insert(
                context.resources.getString(R.string.QQUserDbName),
                null,
                cv)
    }

    private fun insertLoginData(context: Context,qqLoginInfo: QQLoginInfo){
        val cv = ContentValues()
        cv.put(QQLoginDbSchema.RET, qqLoginInfo.ret)
        cv.put(QQLoginDbSchema.PAY_TOKEN, qqLoginInfo.pay_token)
        cv.put(QQLoginDbSchema.PF, qqLoginInfo.pf)
        cv.put(QQLoginDbSchema.EXPIRES_IN, qqLoginInfo.expires_in)
        cv.put(QQLoginDbSchema.OPENID, qqLoginInfo.openid)
        cv.put(QQLoginDbSchema.PFKEY, qqLoginInfo.pfkey)
        cv.put(QQLoginDbSchema.MSG, qqLoginInfo.msg)
        cv.put(QQLoginDbSchema.ACCESS_TOKEN, qqLoginInfo.access_token)
        sqLiteDatabase.insert(
                context.resources.getString(R.string.QQLoginDbName),
                null,
                cv)
    }

    override fun onCancel() {
        Log.i("Cancel","取消")
    }

    override fun onWarning(p0: Int) {
        Log.i("Warning","警告")
    }

    override fun onError(p0: UiError?) {
        Log.i("Error","错误")
    }
}