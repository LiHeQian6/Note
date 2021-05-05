package com.example.note_android.fragment.person

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.note_android.R
import com.example.note_android.about.AboutMeActivity
import com.example.note_android.annotation.Page
import com.example.note_android.bean.Notice
import com.example.note_android.login.LoginActivity
import com.example.note_android.setting.SettingActivity
import com.example.note_android.util.*
import com.google.gson.reflect.TypeToken
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.follow_notice_fragment.*
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.android.synthetic.main.fragment_person.view.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.io.IOException

@Page(name = "个人信息页面")
class PersonFragment : Fragment(),View.OnClickListener {

    private lateinit var personViewModel: PersonViewModel
    private lateinit var root: View
    private lateinit var handler: Handler
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
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if(result != null && result.get("status") == "SUCCESS"){
                    var editor = requireContext().getSharedPreferences(resources.getString(R.string.Authorization),
                            Context.MODE_PRIVATE).edit()
                    editor?.clear()
                    editor?.apply()
                    root.user_name?.setText("立即登录")
                    StateUtil.IF_LOGIN = false
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        StateBarUtils.customFullScreen(requireActivity())
        if(StateUtil.IF_LOGIN) {
            root.user_name?.text = StateUtil.SYSTEM_USER_INFO?.nickName
            root.user_signature?.text = StateUtil.SYSTEM_USER_INFO?.introduction
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
        root.person_image.setOnClickListener(this)
        root.ico_settings.setOnClickListener(this)
        root.user_bac_image.setOnClickListener(this)
        root.my_note.setOnClickListener(this)
        root.my_favourite.setOnClickListener(this)
        root.my_click_up.setOnClickListener(this)
        root.logout_button.setOnClickListener(this)
    }

    private fun checkLogin() {
        if(!StateUtil.IF_LOGIN){
            root.user_name?.text = "立即登录"
        }else{
            root.user_name?.text = StateUtil.SYSTEM_USER_INFO?.nickName
            root.user_signature?.text = StateUtil.SYSTEM_USER_INFO?.introduction
        }
    }

    private fun itemListener() {

    }

    private fun initView() {
        root.person_image.isCircle = true
    }

    private fun logout(){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.logOut().toHttpUrlOrNull()!!.newBuilder()
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .get()
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(), "网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if (result == null || result == "") {
                    Toast.makeText(requireContext(), "网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                handler.sendMessage(mess)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.user_name -> {
                if(StateUtil.IF_LOGIN)
                    return
                else
                    ActivityUtil.get().goActivityResult(requireActivity(),LoginActivity::class.java,SystemCode.QQ_LOGIN_REQUEST)
            }
            R.id.logout_button -> {
                logout()
            }
            R.id.user_bac_image -> {

            }
            R.id.person_image -> {
                if (StateUtil.IF_LOGIN)
                    ActivityUtil.get().goActivity(requireContext(),SettingActivity::class.java,MessageBean("Success","UserMessage",null))
                else
                    Toast.makeText(requireContext(), "请先登录！", Toast.LENGTH_SHORT).show()
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