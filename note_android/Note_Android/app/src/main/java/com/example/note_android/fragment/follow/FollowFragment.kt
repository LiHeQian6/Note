package com.example.note_android.fragment.follow

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.listener.RVItemOnClickListener
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_follow.view.*

class FollowFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel
    private lateinit var root: View
    private var list: MutableList<Any> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followViewModel =
            ViewModelProvider(this).get(FollowViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_follow, container, false)
        initData()
        initRVAdapter()
        initView()
        return root
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.initStatusBarStyle(requireActivity(),false,resources.getColor(R.color.white))
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        var adapter = FollowRVAdapter(list,requireContext(),root.follow_recyclerView)
        root.follow_recyclerView.layoutManager = layoutManager
        root.follow_recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :
            RVItemOnClickListener {
            override fun onItemClick(position: Int) {
                XToast.success(requireContext(),"这是第${position+1}个").show()
            }
        })
    }

    private fun initData() {
        for (i in 1..10){
            list.add(i)
        }
    }

    private fun initView() {
        var title: TextView = root.follow_tool_bar.getChildAt(0) as TextView
        title.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        title.gravity = Gravity.CENTER_HORIZONTAL
        title.textSize = resources.getDimension(R.dimen.sp_6)
        title.typeface = Typeface.DEFAULT
    }
}