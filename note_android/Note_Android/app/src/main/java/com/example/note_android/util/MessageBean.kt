package com.example.note_android.util

import android.os.Parcel
import android.os.Parcelable

data class MessageBean(
    var status: String?,
    var message: String?,
    var data: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(message)
        parcel.writeString(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageBean> {
        override fun createFromParcel(parcel: Parcel): MessageBean {
            return MessageBean(parcel)
        }

        override fun newArray(size: Int): Array<MessageBean?> {
            return arrayOfNulls(size)
        }
    }

}