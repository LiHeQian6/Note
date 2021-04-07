package com.example.note_android.fragment.notice

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.util.StateBarUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.fragment_notice.view.*

@Page(name = "消息页")
class NoticeFragment : Fragment(){

    private lateinit var noticeViewModel: NoticeViewModel
    private lateinit var root: View
    private lateinit var tabList:Array<String>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        noticeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_notice, container, false)
        initView()
        return root
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        //初始化ViewPager2
        root.notice_view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        root.notice_view_pager.adapter = NoticeVPAdapter(requireActivity())

        //初始化TabLayout，并绑定ViewPager2
        root.notice_tab.tabRippleColor = ColorStateList.valueOf(resources.getColor(R.color.transparent,null))
        tabList = resources.getStringArray(R.array.notice_type)
        var tabLayout =  TabLayoutMediator(root.notice_tab,root.notice_view_pager) { tab, position ->
            tab.text = tabList[position]
            var view = layoutInflater.inflate(R.layout.custom_tablayout,null)
            view.findViewById<TextView>(R.id.tab_text).text = tabList[position]
            tab.customView = view
        }
        tabLayout.attach()
        root.notice_tab[SYSTEM].findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.orange,null))
        root.notice_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tab_text)
                        ?.setTextColor(resources.getColor(R.color.orange,null))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tab_text)
                        ?.setTextColor(resources.getColor(R.color.deep_gray,null))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.setStatusBarLightMode(requireActivity())
    }

    companion object {

        const val SYSTEM = 0

        const val LIKE = 1

        const val COMMON = 2

        const val FOLLOW = 3

    }
}