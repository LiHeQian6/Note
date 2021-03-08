package com.example.note_android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.note_android.annotation.Page
import java.io.Serializable

@Page(name = "Intent封装")
class ActivityUtil{

    companion object {
        const val OPEN_ACTIVITY_KEY = "Option" //intent跳转传递传输key
        private var manager: ActivityUtil? = null
        private var intent: Intent? = null
        fun get(): ActivityUtil {
            intent = Intent()
            manager = IntentUtilHolder.INSTANCE
            return manager as ActivityUtil
        }
    }

    /**
     * 内部静态类实现单例线程安全
     */
    private class IntentUtilHolder {
        companion object{
            val INSTANCE = ActivityUtil()
        }
    }

    /**
     * 获取上一个界面传递过来的参数
     *
     * @param activity this
     * @param <T>      泛型
     * @return
    </T> */
    fun <T> getSerializableExtra(activity: Activity?): T? {
        var activity = activity
        val serializable = activity!!.intent.getSerializableExtra(OPEN_ACTIVITY_KEY)
        activity = null
        return serializable as T?
    }

    /**
     * 启动一个Activity
     *
     * @param context
     * @param _class
     */
    fun activity(context: Context?, _class: Class<out Activity?>?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        context.startActivity(intent)
        context = null
    }

    /**
     * 启动activity后关闭所有页面(不刷新页面)
     */
    fun activityKillAll(context: Context?, _class: Class<out Activity?>?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //关掉所要到的界面中间的activity
        intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //设置不要刷新将要跳转的界面
        context.startActivity(intent)
        context = null
    }

    /**
     * 启动activity后kill前一个
     *
     * @param context
     * @param _class
     */
    fun goActivityKill(context: Context?, _class: Class<out Activity?>?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        context.startActivity(intent)
        (context as Activity?)!!.finish()
        context = null
    }

    /**
     * 回调跳转
     *
     * @param context
     * @param _class
     * @param requestCode
     */
    fun goActivityResult(context: Activity?, _class: Class<out Activity?>?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        context.startActivityForResult(intent, requestCode)
        context = null
    }

    /**
     * 回调跳转并finish当前页面
     *
     * @param context
     * @param _class
     * @param requestCode
     */
    fun goActivityResultKill(context: Activity?, _class: Class<out Activity?>?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        context.startActivityForResult(intent, requestCode)
        (context as Activity?)!!.finish()
        context = null
    }

    /**
     * 传递一个序列化实体类
     *
     * @param context
     * @param _class
     * @param serializable
     */
    fun goActivity(context: Context?, _class: Class<out Activity?>?, serializable: Serializable?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        context.startActivity(intent)
        context = null
    }

    /**
     * 启动一个Activity
     *
     * @param context
     * @param _class
     * @param flags
     * @param serializable 传递的实体类
     */
    fun goActivity(context: Context?, _class: Class<out Activity?>?, flags: Int, serializable: Serializable?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        setFlags(flags)
        putSerializable(serializable)
        context.startActivity(intent)
        context = null
    }

    fun goActivityKill(context: Context?, _class: Class<out Activity?>?, serializable: Serializable?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        context.startActivity(intent)
        (context as Activity?)!!.finish()
        context = null
    }

    fun goActivityKill(context: Context?, _class: Class<out Activity?>?, flags: Int, serializable: Serializable?) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        setFlags(flags)
        putSerializable(serializable)
        context.startActivity(intent)
        (context as Activity?)!!.finish()
        context = null
    }

    fun goActivityResult(context: Activity?, _class: Class<out Activity?>?, serializable: Serializable?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        context.startActivityForResult(intent, requestCode)
        context = null
    }

    fun goActivityResult(context: Activity?, _class: Class<out Activity?>?, flags: Int, serializable: Serializable?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        setFlags(flags)
        context.startActivityForResult(intent, requestCode)
        context = null
    }

    fun goActivityResultKill(context: Activity?, _class: Class<out Activity?>?, serializable: Serializable?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        context.startActivityForResult(intent, requestCode)
        context.finish()
        context = null
    }

    fun goActivityResultKill(context: Activity?, _class: Class<out Activity?>?, flags: Int, serializable: Serializable?, requestCode: Int) {
        var context = context
        intent!!.setClass(context!!, _class!!)
        putSerializable(serializable)
        setFlags(flags)
        context.startActivityForResult(intent, requestCode)
        context.finish()
        context = null
    }

    private fun setFlags(flags: Int) {
        if (flags < 0) return
        intent!!.flags = flags
    }

    private fun putSerializable(serializable: Serializable?) {
        if (serializable == null) return
        intent!!.putExtra(OPEN_ACTIVITY_KEY, serializable)

    }
}