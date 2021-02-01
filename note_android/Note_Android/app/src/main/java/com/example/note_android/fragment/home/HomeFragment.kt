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
import com.example.note_android.login.LoginActivity
import com.example.note_android.scan.ScanActivity
import com.xuexiang.xui.utils.StatusBarUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

@Page(name = "主页")
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        StatusBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        root.test_button.setOnClickListener { v: View? ->
            var goLogin = Intent()
            goLogin.setClass(requireContext(),LoginActivity::class.java)
            startActivity(goLogin)
        }
        root.add_button.setOnClickListener { v: View? ->
            var to_edit = Intent()
            to_edit.setClass(requireContext(),EditActivity::class.java)
            startActivity(to_edit)
        }
        root.saoma.setOnClickListener { v: View? ->
            ScanActivity.start(requireActivity(),1, R.style.XQRCodeTheme_Custom)
        }
        return root
    }
}