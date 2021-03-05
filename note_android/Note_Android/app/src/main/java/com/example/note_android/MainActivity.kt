package com.example.note_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.util.*
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tencent.connect.UserInfo
import com.tencent.tauth.Tencent
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var mTencent:Tencent
    private lateinit var navView: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf")
        setContentView(R.layout.activity_main)
//        StateBarUtils.initStatusBarStyle(this,false,resources.getColor(R.color.white))
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
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
        viewPager.offscreenPageLimit = 4
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                nav_view.menu.getItem(position).setChecked(true)
            }
        })
        nav_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId){
                R.id.navigation_home -> {
//                    viewPager.setCurrentItem(0)
                    viewPager.setCurrentItem(0,false)
                }
                R.id.navigation_follow -> {
                    viewPager.setCurrentItem(1,false)
                }
                R.id.navigation_notice -> {
                    viewPager.setCurrentItem(2,false)
                }
                R.id.navigation_persion -> {
                    viewPager.setCurrentItem(3,false)
                }
            }
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SystemCode.QQ_LOGIN_REQUEST){
            mTencent.openId = StateUtil.LOGIN_INFO?.openid
            mTencent.setAccessToken(StateUtil.LOGIN_INFO?.access_token, StateUtil.LOGIN_INFO?.expires_in)
            var user = UserInfo(this,mTencent.qqToken)
            user.getUserInfo(MyIUiListener(applicationContext,"get_user_info"))
        }
    }
}