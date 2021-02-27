package com.example.note_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.note_android.login.QQLogin.MyIUiListener
import com.example.note_android.login.bean.QQUserInfo
import com.example.note_android.util.LoginEvent
import com.example.note_android.util.Single
import com.example.note_android.util.StateUtil
import com.example.note_android.util.SystemCodeUtil
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tencent.connect.UserInfo
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.xuexiang.xui.widget.toast.XToast
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var mTencent:Tencent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTencent = Tencent.createInstance(resources.getString(R.string.APP_ID),applicationContext)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_follow, R.id.navigation_persion
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.findViewById<View>(R.id.navigation_home).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_follow).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_notice).setOnLongClickListener { true }
        navView.findViewById<View>(R.id.navigation_persion).setOnLongClickListener { true }
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navView.setOnNavigationItemReselectedListener { true }
        navView.setupWithNavController(navController)
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