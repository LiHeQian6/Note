package com.example.note_android.fragment.follow

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_follow.view.*

class FollowFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel
    private lateinit var root: View
    private var list: MutableList<Any> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followViewModel =
            ViewModelProvider(this).get(FollowViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_follow, container, false)
        fitSystem()
        initData()
        initRVAdapter()
        initView()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun fitSystem(){
        var decorView = requireActivity().window.decorView;
        var windowInsets = decorView.rootWindowInsets
        if (windowInsets != null) {
            root.dispatchApplyWindowInsets(windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), 0, 0))
            root.setOnApplyWindowInsetsListener{v: View?, insets: WindowInsets? -> insets }
        }
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.setStatusBarLightMode(requireActivity())
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        var adapter = FollowRVAdapter(list,requireContext(),root.follow_recyclerView)
        var divider = DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.rv_divider,null))
        root.follow_recyclerView.addItemDecoration(divider)
        root.follow_recyclerView.layoutManager = layoutManager
        root.follow_recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :
            OnItemClickListener {
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