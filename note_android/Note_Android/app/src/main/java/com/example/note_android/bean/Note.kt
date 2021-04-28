package com.example.note_android.bean

data class Note(var title:String,
                var content:String,
                var tags:MutableList<Tag>,
                var type:NoteType)
