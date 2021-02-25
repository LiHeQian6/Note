package com.example.note_android.fragment.person

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.login.LoginActivity
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.util.ActivityUtil
import com.example.note_android.util.Single
import com.example.note_android.util.StateUtil
import com.example.note_android.util.SystemCodeUtil
import com.tencent.connect.UserInfo
import com.tencent.tauth.Tencent
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_person.view.*
import java.util.*

@Page(name = "个人信息页面")
class PersonFragment : Fragment(),View.OnClickListener {

    private lateinit var personViewModel: PersonViewModel
    private lateinit var settingAdapter : SettingAdapter
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_person, container, false)
        initView()
        checkLogin()
        itemListener()
        initListener()
        return root
    }

    private fun initListener() {
        root.logout_button.setOnClickListener(this)
        root.user_name.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if(StateUtil.IF_LOGIN) {
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
            root.logout_button.setText("退 出 登 陆")
        }
    }

    private fun checkLogin() {
        if(!StateUtil.IF_LOGIN){
            root.user_name?.setText("立即登录")
            root.logout_button.setText("请 登 陆")
        }else{
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
            root.logout_button.setText("退 出 登 陆")
        }
    }

    private fun itemListener() {
        root.setting_list?.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0 -> {
                    XToast.info(requireContext(),"正在开发").show()
                }
                1 -> {
                    showSelectDialog()
                }
                2 -> {
                    XToast.info(requireContext(),"正在开发").show()
                }
            }
        }
    }

    private fun showSelectDialog() {
        val list: MutableList<MaterialSimpleListItem> = ArrayList()
        list.add(MaterialSimpleListItem.Builder(context)
                .content(R.string.tip_richText)
                .icon(R.drawable.ico_rich_text)
                .build())
        list.add(MaterialSimpleListItem.Builder(context)
                .content(R.string.tip_markdown)
                .icon(R.drawable.ico_markdown)
                .build())
        val adapter = MaterialSimpleListAdapter(list)
        adapter.setOnItemClickListener { dialog, index, item ->
            when(index) {
                0 -> {
                    Log.i("编辑器",SystemCodeUtil.RICH_TEXT)
                    StateUtil.EDITOR = SystemCodeUtil.RICH_TEXT
                }
                1 -> {
                    Log.i("编辑器",SystemCodeUtil.MARKDOWN)
                    StateUtil.EDITOR = SystemCodeUtil.MARKDOWN
                }
            }
        }
        MaterialDialog.Builder(requireContext()).adapter(adapter, null).show()
    }

    private fun initView() {
        val itemArray: Array<String> = resources.getStringArray(R.array.persion_setting)
        settingAdapter = SettingAdapter(itemArray,requireContext(),R.layout.setting_item)
        root.setting_list.adapter = settingAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout_button -> {
                if(!StateUtil.IF_LOGIN)
                    return
                var editor = Single.getShared(requireContext())?.edit()
                editor?.clear()
                editor?.commit()
                dataBaseHelper = DataBaseHelper(requireContext(),1)
                sqLiteDatabase = dataBaseHelper.writableDatabase
                sqLiteDatabase.execSQL("delete from ${resources.getString(R.string.QQLoginDbName)}")
                sqLiteDatabase.execSQL("delete from ${resources.getString(R.string.QQUserDbName)}")
                root.user_name?.setText("立即登录")
                root.logout_button.setText("请 登 录")
                StateUtil.IF_LOGIN = false
            }
            R.id.user_name -> {
                if(StateUtil.IF_LOGIN)
                    return
                else
                    ActivityUtil.get()?.activity(requireContext(),LoginActivity::class.java)
            }
        }
    }

}