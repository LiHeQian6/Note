package com.example.note_android.bean

import com.example.note_android.bean.UserInfo
import java.util.*

data class Notice(var id:String,
                  var from:UserInfo,
                  var to:UserInfo,
                  var msType:String,
                  var content:String,
                  var createTime:Date)