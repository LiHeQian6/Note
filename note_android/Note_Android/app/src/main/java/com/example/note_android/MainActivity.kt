package com.example.note_android

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.note_android.fragment.follow.FollowFragment
import com.example.note_android.fragment.home.HomeFragment
import com.example.note_android.fragment.notice.NoticeFragment
import com.example.note_android.fragment.person.PersonFragment
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.login.bean.QQUserInfo
import com.example.note_android.util.*
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tencent.connect.UserInfo
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var mTencent:Tencent
    private lateinit var navView: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        StateBarUtils.initStatusBarStyle(this,true,resources.getColor(R.color.orange))
        navView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
        initNavView()
        initViewPage()
    }

    private fun initNavView() {
        navView.findViewById<View>(R.id.navigation_home).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_follow).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_notice).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_persion).setOnLongClickListener { true }
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navView.setOnNavigationItemReselectedListener { true }
    }

    private fun initViewPage() {
        viewPager = findViewById(R.id.nav_host_fragment);
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                nav_view.menu.getItem(position).setChecked(true)
            }
        })
        nav_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId){
                R.id.navigation_home -> {
                    viewPager.setCurrentItem(0)
                }
                R.id.navigation_follow -> {
                    viewPager.setCurrentItem(1)
                }
                R.id.navigation_notice -> {
                    viewPager.setCurrentItem(2)
                }
                R.id.navigation_persion -> {
                    viewPager.setCurrentItem(3)
                }
            }
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SystemCodeUtil.QQ_LOGIN_REQUEST){
            mTencent.openId = StateUtil.LOGIN_INFO?.openid
            mTencent.setAccessToken(StateUtil.LOGIN_INFO?.access_token, StateUtil.LOGIN_INFO?.expires_in)
            var user = UserInfo(this,mTencent.qqToken)
            user.getUserInfo(MyIUiListener(applicationContext,"get_user_info"))
        }
    }
}