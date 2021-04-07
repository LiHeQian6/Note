package com.example.note_android.fragment.notice

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.note_android.fragment.notice.type.CommonNoticeFragment
import com.example.note_android.fragment.notice.type.FollowNoticeFragment
import com.example.note_android.fragment.notice.type.LikeNoticeFragment
import com.example.note_android.fragment.notice.type.SystemNoticeFragment

class NoticeVPAdapter : FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    private val typeFragments: SparseArray<Fragment> = SparseArray()

    init {
        typeFragments.put(PAGE_SYSTEM, SystemNoticeFragment())
        typeFragments.put(PAGE_LIKE, LikeNoticeFragment())
        typeFragments.put(PAGE_COMMON, CommonNoticeFragment())
        typeFragments.put(PAGE_FOLLOW, FollowNoticeFragment())
    }

    override fun getItemCount(): Int {
        return typeFragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return typeFragments[position]
    }

    companion object {

        const val PAGE_SYSTEM = 0

        const val PAGE_LIKE = 1

        const val PAGE_COMMON = 2

        const val PAGE_FOLLOW = 3

    }
}