package com.example.note_android.about.fragment.my_favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyLikeViewModel : ViewModel() {

    private var myLikeList = MutableLiveData<MutableList<Int>>()


    fun initData(): MutableLiveData<MutableList<Int>> {
        var list : MutableList<Int> = ArrayList()
        for (i in 1..10){
            list.add(i)
        }
        myLikeList.postValue(list)
        return myLikeList
    }
}