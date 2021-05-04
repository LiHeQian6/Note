package com.example.note_android.bean

import java.io.Serializable
import java.util.*

data class Comment (
    var child: MutableList<Comment>? = null,
    var content: String? = null,
    var createTime: Date? = null,
    var id: Int = 0,
    var like: Boolean = false,
    var likeNum: Int = 0,
    var to: To? = null,
    var user: UserInfo? = null,
): Serializable