package com.example.note_android.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.fragment.home.MainRVAdapter

interface RVItemOnClickListener{
    fun onItemClick(
        position: Int
    )
}