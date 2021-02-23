package com.example.note_android.login.QQLogin

import android.content.Context
import android.util.Log
import com.example.note_android.MainActivity
import com.example.note_android.util.ActivityUtil
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast

class MyIUiListener(var context: Context) : IUiListener {

    override fun onComplete(p0: Any?) {
        Log.i("结果:",p0.toString())
        ActivityUtil.get()?.goActivityKill(context, MainActivity::class.java)
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