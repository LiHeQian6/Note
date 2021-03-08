package com.example.note_android.about.fragment.my_favourite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.about.fragment.my_like_up.MyClickUpAdapter
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView
import kotlinx.android.synthetic.main.my_click_up_fragment.*
import kotlinx.android.synthetic.main.my_like_fragment.*

class MyLikeFragment : Fragment() {

    companion object {
        fun newInstance() = MyLikeFragment()
    }

    private lateinit var viewModel: MyLikeViewModel
    private lateinit var myLikeRVAdapter: MyLikeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_like_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyLikeViewModel::class.java)
        var dataList = viewModel.initData()
        dataList.observe(viewLifecycleOwner,
            Observer<MutableList<Int>> {
                initAdapter(it)
            })
    }

    private fun initAdapter(list: MutableList<Int>) {
        myLikeRVAdapter = MyLikeRVAdapter(list,my_like_recycler_view)
        var layoutManager = LinearLayoutManager(requireContext())
        my_like_recycler_view.layoutManager = layoutManager
        my_like_recycler_view.adapter = myLikeRVAdapter
        myLikeRVAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                //TODO 跳转笔记展示页面
            }
        })
        myLikeRVAdapter.setLongClickListener(object : OnItemLongClickListener {
            override fun onLongClick(position: Int) {
                showBottomList(position)
            }
        })
    }

    private fun showBottomList(itemPosition: Int){
        BottomSheet.BottomListSheetBuilder(activity)
            .addItem("删除")
            .addItem("分享")
            .addItem("编辑")
            .addItem("关闭")
            .setIsCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when(position){
                    0 -> {
                        dialog.dismiss()
                        myLikeRVAdapter.removeItem(itemPosition)
                    }
                    1 -> {
                        dialog.dismiss()
                        showBottomGrid()
                    }
                    2 -> {

                    }
                    3 -> {
                        dialog.dismiss()
                    }
                }
            }.build().show()
    }

    private fun showBottomGrid(){
        val TAG_SHARE_QQ = 0
        val TAG_SHARE_QQ_SPACE = 1
        val TAG_SHARE_WECHAT = 2
        val TAG_SHARE_WECHAT_SPACE = 3
        val builder = BottomSheet.BottomGridSheetBuilder(activity)
        builder
            .addItem(
                R.drawable.ico_qq,
                "分享给好友",
                TAG_SHARE_QQ,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_qq_space,
                "分享到空间",
                TAG_SHARE_QQ_SPACE,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat,
                "分享到微信",
                TAG_SHARE_WECHAT,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat_space,
                "分享到朋友圈",
                TAG_SHARE_WECHAT_SPACE,
                BottomSheet.BottomGridSheetBuilder.FIRST_LINE
            )
            .setOnSheetItemClickListener { dialog: BottomSheet, itemView: BottomSheetItemView ->
                dialog.dismiss()
                Toast.makeText(requireContext(),"功能正在开发", Toast.LENGTH_SHORT).show()
            }.build().show()
    }
}