package com.example.note_android.fragment.notice

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.listener.OnItemClickListener
import com.example.note_android.util.StateBarUtils
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_notice.view.*

@Page(name = "消息页")
class NoticeFragment : Fragment(),View.OnClickListener {

    private lateinit var noticeViewModel: NoticeViewModel
    private lateinit var root: View
    private var list: MutableList<Notice> = ArrayList()
    private lateinit var adapter: NoticeRVAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noticeViewModel =
            ViewModelProvider(this).get(NoticeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_notice, container, false)
         fitSystem()
        initToolBar()
        initData()
        initRVAdapter()
        initListener()
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

    private fun initToolBar() {

    }

    private fun initData() {
        for (i in 1..20){
            list.add(Notice("李和谦","你好，大佬","2021-2-28"))
        }
    }

    private fun initRVAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        adapter = NoticeRVAdapter(list,requireContext(),root.recyclerView)
        root.recyclerView.layoutManager = layoutManager
        root.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        root.recyclerView.adapter = adapter
    }

    private fun initListener() {
        adapter.setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                XToast.success(requireContext(),"这是第${position+1}个").show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }
}