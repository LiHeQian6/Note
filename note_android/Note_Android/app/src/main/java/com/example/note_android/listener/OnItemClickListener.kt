package com.example.note_android.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.fragment.home.MainRVAdapter

interface OnItemClickListener{
    fun onItemClick(
        position: Int
    )
}