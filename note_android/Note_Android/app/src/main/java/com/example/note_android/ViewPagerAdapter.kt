package com.example.note_android

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.note_android.fragment.follow.FollowFragment
import com.example.note_android.fragment.home.HomeFragment
import com.example.note_android.fragment.notice.NoticeFragment
import com.example.note_android.fragment.person.PersonFragment
import java.util.Calendar.getInstance

class ViewPagerAdapter: FragmentStateAdapter {

    constructor(fragment: Fragment) : super(fragment)

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    private val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(PAGE_HOME, HomeFragment())
        fragments.put(PAGE_FIND, FollowFragment())
        fragments.put(PAGE_INDICATOR, NoticeFragment())
        fragments.put(PAGE_OTHERS, PersonFragment())
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    companion object {

        const val PAGE_HOME = 0

        const val PAGE_FIND = 1

        const val PAGE_INDICATOR = 2

        const val PAGE_OTHERS = 3

    }
}