package com.example.note_android.edit

import android.view.View
import com.example.note_android.R
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.activity_edit.*

class EditListener : View.OnClickListener {

    var richEditor:RichEditor

    constructor(view: RichEditor){
        this.richEditor = view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.text_bold -> {
                richEditor.setBold()
            }
            R.id.text_lean -> {
                richEditor.setItalic()
            }
        }
    }

}