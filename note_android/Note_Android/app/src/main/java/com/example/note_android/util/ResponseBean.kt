package com.example.note_android.util

import java.io.Serializable

data class ResponseBean(var status: String,
                        var message: String,
                        var data: Any) : Serializable