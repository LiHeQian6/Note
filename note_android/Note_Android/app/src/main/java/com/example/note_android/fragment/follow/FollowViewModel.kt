package com.example.note_android.fragment.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FollowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是关注页"
    }
    val text: LiveData<String> = _text
}