package com.example.note_android.fragment.follow

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.util.StateBarUtils
import kotlinx.android.synthetic.main.fragment_follow.view.*

class FollowFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followViewModel =
            ViewModelProvider(this).get(FollowViewModel::class.java)
        StateBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        root = inflater.inflate(R.layout.fragment_follow, container, false)
        initView()
        return root
    }

    private fun initView() {
        var title: TextView = root.follow_tool_bar.getChildAt(0) as TextView
        title.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        title.gravity = Gravity.CENTER_HORIZONTAL
        title.textSize = resources.getDimension(R.dimen.sp_6)
        title.typeface = Typeface.DEFAULT
    }
}