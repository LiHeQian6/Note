package com.example.note_android.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.edit.EditActivity
import com.example.note_android.fragment.notice.NoticeViewModel
import com.example.note_android.scan.ScanActivity
import com.example.note_android.util.ActivityUtil
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.fragment_home.view.*

@Page(name = "主页")
class HomeFragment : Fragment(),View.OnClickListener {

    private lateinit var homeViewModel: NoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        StatusBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        initListener(root)
        return root
    }

    private fun initListener(view: View) {
        view.add_button.setOnClickListener(this)
        view.saoma.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("123","123")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_button -> {
                ActivityUtil.get()?.activity(requireContext(),EditActivity::class.java)
            }
            R.id.saoma -> {
                ScanActivity.start(requireActivity(),1, R.style.XQRCodeTheme_Custom)
            }
        }
    }


}