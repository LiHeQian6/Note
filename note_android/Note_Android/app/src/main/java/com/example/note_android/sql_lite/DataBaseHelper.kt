package com.example.note_android.sql_lite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.note_android.R

class DataBaseHelper : SQLiteOpenHelper {

    private var context: Context? = null

    companion object{
        const val DATABASE_NAME:String = "login_info"
    }

    constructor(context: Context?, version: Int) : super(context, DATABASE_NAME, null, version){
        this.context = context
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sqlQQLoginTable = "create table ${context?.resources?.getString(R.string.QQLoginDbName)} " +
                "(id Integer primary key autoincrement," +
                "${QQLoginDbSchema.RET}," +
                "${QQLoginDbSchema.PAY_TOKEN}," +
                "${QQLoginDbSchema.PF}," +
                "${QQLoginDbSchema.EXPIRES_IN}," +
                "${QQLoginDbSchema.OPENID}," +
                "${QQLoginDbSchema.PFKEY}," +
                "${QQLoginDbSchema.MSG}," +
                QQLoginDbSchema.ACCESS_TOKEN +
                ")"

        val sqlQQUserTable = "create table ${context?.resources?.getString(R.string.QQUserDbName)} " +
                "(id Integer primary key autoincrement," +
                "${QQUserDbSchema.IS_YELLOW_YEAR_VIP}," +
                "${QQUserDbSchema.RET}," +
                "${QQUserDbSchema.FIGUREURL_QQ_1}," +
                "${QQUserDbSchema.FIGUREURL_QQ_2}," +
                "${QQUserDbSchema.NICKNAME}," +
                "${QQUserDbSchema.YELLOW_VIP_LEVEL}," +
                "${QQUserDbSchema.MSG}," +
                "${QQUserDbSchema.FIGUREURL_1}," +
                "${QQUserDbSchema.VIP}," +
                "${QQUserDbSchema.LEVEL}," +
                "${QQUserDbSchema.FIGUREURL_2}," +
                "${QQUserDbSchema.IS_YELLOW_VIP}," +
                "${QQUserDbSchema.GENDER}," +
                QQUserDbSchema.FIGUREURL +
                ")"
        db.execSQL(sqlQQLoginTable)
        db.execSQL(sqlQQUserTable)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}