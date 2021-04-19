package com.example.note_android.note

data class Common(var name:String,
                  var content:String,
                  var zan:String,
                  var com:String,
                  var child:MutableList<Reply>
)