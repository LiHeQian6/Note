package com.example.note_android.bean

import java.io.Serializable

data class NoteType(
        var id: String,
        var name: String,
        var child: MutableList<NoteType>?
        ) : Serializable
