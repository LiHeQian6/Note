package com.example.note_android.util

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class MessageBean(
    var status: String?,
    var message: String?,
    var data: MutableList<Any>?
) : Serializable