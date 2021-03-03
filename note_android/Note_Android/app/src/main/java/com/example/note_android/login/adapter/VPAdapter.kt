package com.example.note_android.login.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.note_android.ViewPagerAdapter
import com.example.note_android.login.fragment.ForgetFragment
import com.example.note_android.login.fragment.ResetFragment

class VPAdapter: FragmentStateAdapter {

    private val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(ViewPagerAdapter.PAGE_HOME, ForgetFragment())
        fragments.put(ViewPagerAdapter.PAGE_FIND, ResetFragment())
    }

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    override fun getItemCount(): Int {
        return fragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}