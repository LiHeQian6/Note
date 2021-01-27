package com.example.note_android.fragment.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是个人信息页面"
    }
    val text: LiveData<String> = _text
}