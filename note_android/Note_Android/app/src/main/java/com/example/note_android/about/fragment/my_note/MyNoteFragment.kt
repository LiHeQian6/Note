package com.example.note_android.about.fragment.my_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.listener.OnItemLongClickListener
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet.BottomGridSheetBuilder
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView
import kotlinx.android.synthetic.main.my_note_fragment.*

class MyNoteFragment : Fragment() {

    companion object {
        fun newInstance() =
            MyNoteFragment()
    }

    private lateinit var viewModel: MyNoteViewModel
    private var list:MutableList<Int> = ArrayList()
    private lateinit var myNoteRVAdapter: MyNoteRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNoteViewModel::class.java)
        // TODO: Use the ViewModel
        initView()
        initData()
        initRV()
    }

    private fun initData(){

    }

    private fun initView(){
        for (i in 1..10){
            list.add(i)
        }
    }

    private fun initRV(){
        var layoutManager = LinearLayoutManager(requireContext())
        myNoteRVAdapter = MyNoteRVAdapter(list,my_note_recycler_view)
        my_note_recycler_view.layoutManager = layoutManager
        my_note_recycler_view.adapter = myNoteRVAdapter
        myNoteRVAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                //TODO 跳转笔记展示页面
            }
        })
        myNoteRVAdapter.setLongClickListener(object : OnItemLongClickListener{
            override fun onLongClick(position: Int) {
                showBottomList(position)
            }
        })
    }

    private fun showBottomList(itemPosition: Int){
        BottomSheet.BottomListSheetBuilder(requireActivity())
            .addItem("删除")
            .addItem("分享")
            .addItem("编辑")
            .addItem("关闭")
            .setIsCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when(position){
                    0 -> {
                        dialog.dismiss()
                        myNoteRVAdapter.removeItem(itemPosition)
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
        val builder = BottomGridSheetBuilder(activity)
        builder
            .addItem(
                R.drawable.ico_qq,
                "分享给好友",
                TAG_SHARE_QQ,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_qq_space,
                "分享到空间",
                TAG_SHARE_QQ_SPACE,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat,
                "分享到微信",
                TAG_SHARE_WECHAT,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ico_wechat_space,
                "分享到朋友圈",
                TAG_SHARE_WECHAT_SPACE,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .setOnSheetItemClickListener { dialog: BottomSheet, itemView: BottomSheetItemView ->
                dialog.dismiss()
                Toast.makeText(requireContext(),"功能正在开发",Toast.LENGTH_SHORT).show()
            }.build().show()
    }
}