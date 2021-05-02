/**
 * Copyright 2021 bejson.com
 */
package com.example.note_android.bean

data class UserInfo(
    var id:Int = 0,
    var username: String? = null,
    var introduction: String? = null,
    var nickName: String? = null,
    var photo: String? = null,
    var certification: String? = null,
    var like:Int = 0,
    var enabled:Boolean = false,
    var followee:Int = 0,
    var follower:Int = 0,
    var follow:Boolean = false
)