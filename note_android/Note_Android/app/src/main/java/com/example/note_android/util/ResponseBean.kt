package com.example.note_android.util

import java.io.Serializable

data class ResponseBean(var status: Status,
                        var message: String,
                        var data: Any) : Serializable