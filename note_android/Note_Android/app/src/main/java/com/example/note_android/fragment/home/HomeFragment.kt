package com.example.note_android.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.edit.EditActivity
import com.example.note_android.fragment.RVItemOnClickListener
import com.example.note_android.fragment.notice.NoticeViewModel
import com.example.note_android.scan.ScanActivity
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_home.view.*

@Page(name = "主页")
class HomeFragment : Fragment(),View.OnClickListener {

    private lateinit var homeViewModel: NoticeViewModel
    private lateinit var root: View
    private var list: MutableList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        StateBarUtils.initStatusBarStyle(requireActivity(),true,resources.getColor(R.color.orange))
        homeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        initData()
        initRVAdapter()
        initListener()
        return root
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        var adapter = MainRVAdapter(list,requireContext(),root.home_recyclerView)
        root.home_recyclerView.layoutManager = layoutManager
//        root.home_recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))

        root.home_recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : RVItemOnClickListener{
            override fun onItemClick(position: Int) {
                XToast.success(requireContext(),"这是第${position+1}个").show()
            }
        })
//        root.home_recyclerView.addOnItemTouchListener()
    }

    private fun initData() {
        for (i in 1..10){
            list.add(i)
        }
    }

    private fun initListener() {
        root.add_button.setOnClickListener(this)
        root.saoma.setOnClickListener(this)
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