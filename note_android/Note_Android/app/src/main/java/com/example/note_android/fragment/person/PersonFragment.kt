package com.example.note_android.fragment.person

import android.content.Context
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
import com.example.note_android.about.AboutMeActivity
import com.example.note_android.annotation.Page
import com.example.note_android.login.LoginActivity
import com.example.note_android.util.*
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.android.synthetic.main.fragment_person.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Page(name = "个人信息页面")
class PersonFragment : Fragment(),View.OnClickListener {

    private lateinit var personViewModel: PersonViewModel
    private lateinit var root: View
//    private lateinit var mTencent:Tencent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_person, container, false)
//        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),requireContext())
        EventBus.getDefault().register(this)
        initView()
        checkLogin()
        itemListener()
        initListener()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.customFullScreen(requireActivity())
        if(StateUtil.IF_LOGIN && StateUtil.initInfo(requireContext())) {
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
            Glide.with(requireContext()).load(StateUtil.USER_INFO?.figureurl_qq_2)
                    .error(R.drawable.head_img)
                    .placeholder(R.drawable.head_img)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .into(root.person_image)
        }else{
            root.user_name?.setText("立即登录")
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
                .error(R.drawable.head_img)
                .placeholder(R.drawable.head_img)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(root.person_image)
        EventBus.getDefault().unregister(this)
    }

    private fun initListener() {
        root.user_name.setOnClickListener(this)
        root.ico_settings.setOnClickListener(this)
        root.user_bac_image.setOnClickListener(this)
        root.my_note.setOnClickListener(this)
        root.my_favourite.setOnClickListener(this)
        root.my_click_up.setOnClickListener(this)
    }

    private fun checkLogin() {
        if(!StateUtil.IF_LOGIN){
            root.user_name?.setText("立即登录")
        }else{
            root.user_name?.setText(StateUtil.USER_INFO?.nickname)
        }
    }

    private fun itemListener() {

    }

    private fun initView() {

        root.person_image.isCircle = true
    }

//    private fun logout(){
//        if(!StateUtil.IF_LOGIN)
//            return
//        var editor = requireContext().getSharedPreferences(resources.getString(R.string.LoginInfo),
//            Context.MODE_PRIVATE).edit()
//        editor?.clear()
//        editor?.apply()
//        root.user_name?.setText("立即登录")
//        StateUtil.IF_LOGIN = false
//        mTencent.logout(requireContext())
//    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.user_name -> {
                if(StateUtil.IF_LOGIN)
                    return
                else
                    ActivityUtil.get().goActivityResult(requireActivity(),LoginActivity::class.java,SystemCode.QQ_LOGIN_REQUEST)
            }
            R.id.ico_settings -> {

            }
            R.id.user_bac_image -> {
            }
            R.id.my_note -> {
                ActivityUtil.get().goActivity(requireContext(),AboutMeActivity::class.java,MessageBean("Success","MyNote",null))
            }
            R.id.my_favourite -> {
                ActivityUtil.get().goActivity(requireContext(),AboutMeActivity::class.java,MessageBean("Success","MyLike",null))
            }
            R.id.my_click_up -> {
                ActivityUtil.get().goActivity(requireContext(),AboutMeActivity::class.java,MessageBean("Success","MyClickUp",null))
            }
        }
    }
}