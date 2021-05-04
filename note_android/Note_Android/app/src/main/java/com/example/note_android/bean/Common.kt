package com.example.note_android.bean

import com.example.note_android.note.Reply

data class Common(var content:String,
                  var note:NoteInfo,
                  var parentId: String
)