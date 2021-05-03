package com.example.note_android.bean

import java.io.Serializable
import java.util.*
import kotlin.ConcurrentModificationException

data class NoteInfo (
    var id:Int = 0,
    var title: String? = null,
    var content: String? = null,
    var like:Int = 0,
    var look:Int = 0,
    var collect:Int = 0,
    var popularity: Long = 0,
    var commentNum: Int,
    var comments: MutableList<Comment>? = null,
    var createTime: Date? = null,
    var user: UserInfo? = null,
    var type: NoteType? = null,
    var tags: MutableList<Tag>? = null,
    var liked: Boolean = false,
    var collected: Boolean = false
): Serializable