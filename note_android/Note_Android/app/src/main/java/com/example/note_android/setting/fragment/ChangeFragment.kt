package com.example.note_android.setting.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xui.widget.dialog.strategy.InputInfo
import kotlinx.android.synthetic.main.change_fragment.*

class ChangeFragment : Fragment(),View.OnClickListener {

    companion object {
        const val CHANGE_NAME = 0
        const val CHANGE_GRAPH = 1

        fun newInstance() = ChangeFragment()
    }

    private lateinit var viewModel: ChangeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.change_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ChangeViewModel::class.java)

        initListener()
    }

    private fun initListener() {
        change_user_name.setOnClickListener(this)
        change_user_autograph.setOnClickListener(this)
    }

    private fun showDialog(type:Int){

        var hint:String
        var title:String

        when(type){
            CHANGE_NAME -> {
                hint = "请输入新的昵称"
                title = "修改昵称"
            }
            CHANGE_GRAPH -> {
                hint = "请输入新的签名"
                title = "修改签名"
            }
            else -> {
                hint = "参数不合法"
                title = "参数不合法"
            }
        }

        DialogLoader.getInstance().showInputDialog(
                requireContext(),
                R.drawable.rv_divider,
                title,
                "",
                InputInfo(InputType.TYPE_CLASS_TEXT,hint),
                null,
                "修改",
                DialogInterface.OnClickListener { dialog, which ->

                },
                "取消",
                null)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.change_user_name -> {
                showDialog(CHANGE_NAME)
            }
            R.id.change_user_autograph -> {
                showDialog(CHANGE_GRAPH)
            }
            R.id.save_change_button -> {

            }
        }
    }
}