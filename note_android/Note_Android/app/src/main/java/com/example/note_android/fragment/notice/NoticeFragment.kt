package com.example.note_android.fragment.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.edit.EditActivity
import com.example.note_android.scan.ScanActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.fragment_home.view.*

@Page(name = "消息页")
class NoticeFragment : Fragment(),View.OnClickListener {

    private lateinit var noticeViewModel: NoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noticeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        StateBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        val root = inflater.inflate(R.layout.fragment_notice, container, false)
        initListener(root)
        return root
    }

    private fun initListener(view: View) {

    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }


}