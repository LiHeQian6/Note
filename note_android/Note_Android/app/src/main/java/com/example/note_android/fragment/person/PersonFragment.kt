package com.example.note_android.fragment.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.login.LoginActivity
import com.example.note_android.sql_lite.DataBaseHelper
import com.example.note_android.util.*
import com.tencent.tauth.Tencent
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.fragment_person.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@Page(name = "个人信息页面")
class PersonFragment : Fragment(),View.OnClickListener {

    private lateinit var personViewModel: PersonViewModel
    private lateinit var settingAdapter : SettingAdapter
    private lateinit var root: View
    private lateinit var mTencent:Tencent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_person, container, false)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),requireContext())
        EventBus.getDefault().register(this)
        initView()
        checkLogin()
        itemListener()
        initListener()
        return root
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.translucent(requireActivity())
        StateUtil.initInfo(requireContext())
        if(StateUtil.IF_LOGIN) {
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
            root.logout_button.visibility = View.VISIBLE
            Glide.with(requireContext()).load(StateUtil.USER_INFO?.figureurl_qq_2)
                    .error(R.drawable.head_1)
                    .placeholder(R.drawable.head_1)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .into(root.person_image)
        }else{
            root.user_name?.setText("立即登录")
            root.logout_button.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("注销","EventBus注销")
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun setInfoEvent(userEvent: UserEvent){
        root.user_name?.setText(userEvent.userInfo?.nickname)
        Glide.with(requireContext()).load(StateUtil.USER_INFO?.figureurl_qq_1)
                .error(R.drawable.head_1)
                .placeholder(R.drawable.head_1)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(root.person_image)
        EventBus.getDefault().unregister(this)
    }

    private fun initListener() {
        root.logout_button.setOnClickListener(this)
        root.user_name.setOnClickListener(this)
    }

    private fun checkLogin() {
        if(!StateUtil.IF_LOGIN){
            root.user_name?.setText("立即登录")
            root.logout_button.visibility = View.GONE
        }else{
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
            root.logout_button.visibility = View.VISIBLE
        }
    }

    private fun itemListener() {
//        root.setting_list?.setOnItemClickListener { parent, view, position, id ->
//            when(position){
//                0 -> {
//                    XToast.info(requireContext(),"正在开发").show()
//                }
//                1 -> {
//                    showSelectDialog()
//                }
//                2 -> {
//                    XToast.info(requireContext(),"正在开发").show()
//                }
//            }
//        }
    }

    private fun initView() {
//        val itemArray: Array<String> = resources.getStringArray(R.array.persion_setting)
//        settingAdapter = SettingAdapter(itemArray,requireContext(),R.layout.setting_item)
//        root.setting_list.adapter = settingAdapter

        root.person_image.isCircle = true
    }

    private fun logout(){
        if(!StateUtil.IF_LOGIN)
            return
        var editor = Single.getShared(requireContext())?.edit()
        editor?.clear()
        editor?.apply()
        root.user_name?.setText("立即登录")
        root.logout_button.visibility = View.GONE
        StateUtil.IF_LOGIN = false
        mTencent.logout(requireContext())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout_button -> {
                logout()
            }
            R.id.user_name -> {
                if(StateUtil.IF_LOGIN)
                    return
                else
                    ActivityUtil.get()?.goActivityResult(requireActivity(),LoginActivity::class.java,SystemCodeUtil.QQ_LOGIN_REQUEST)
            }
        }
    }

    private fun initSystemUi(){
        StateBarUtils.initStatusBarStyle(requireActivity(),false,resources.getColor(R.color.white))
    }
}