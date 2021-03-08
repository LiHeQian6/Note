package com.example.note_android.about.fragment.my_like_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyClickUpViewModel : ViewModel() {

    private var myClickList = MutableLiveData<MutableList<Int>>()


    fun initData(): MutableLiveData<MutableList<Int>>{
        var list : MutableList<Int> = ArrayList()
        for (i in 1..10){
            list.add(i)
        }
        myClickList.postValue(list)
        return myClickList
    }
}