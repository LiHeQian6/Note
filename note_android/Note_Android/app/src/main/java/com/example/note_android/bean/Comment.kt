package com.example.note_android.bean

import java.io.Serializable

data class Comment (
    var child: List<Comment>? = null,
    var content: String? = null,
    var createTime: String? = null,
    var id: Int = 0,
    var like: Boolean = false,
    var likeNum: Int = 0,
    var to: To? = null,
    var user: UserInfo? = null,
): Serializable