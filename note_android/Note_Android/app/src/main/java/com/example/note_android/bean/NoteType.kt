package com.example.note_android.bean

data class NoteType(var id:String,var name:String,var child:MutableList<NoteType>?)
