package com.example.note_android.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.note_android.listener.HttpListener
import com.example.note_android.R
import com.example.note_android.databinding.ActivityRegisterBinding
import com.example.note_android.login.registerModel.RegisterVM
import com.example.note_android.util.SystemCode
import com.xuexiang.xui.XUI

class RegisterActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var dataBinding:ActivityRegisterBinding
    private lateinit var registerVM: RegisterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.init(application)
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf")
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        registerVM = RegisterVM(this)
        initView()
        initListener()
    }

    private fun initView() {
        dataBinding.getVerifyCode.setCountDownTime(30 * 1000)
    }

    private fun initListener() {
        dataBinding.getVerifyCode.setOnClickListener(this)
        dataBinding.registerButton.setOnClickListener(this)
        dataBinding.getVerifyCode.setOnClickListener(this)
        registerVM.setHttpListener(object :
            HttpListener {
            override fun complete(dataType: String, data: String) {
                if(data == "Success"){
                    var intent = Intent()
                    intent.putExtra("email",dataBinding.email.text)
                    intent.putExtra("password",dataBinding.password.text)
                    setResult(SystemCode.REGISTER,intent)
                    finish()
                }
            }
        })
    }

    private fun checkData():Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(dataBinding.email.text).matches()){
            Toast.makeText(applicationContext,"邮箱格式不合法！",Toast.LENGTH_SHORT).show()
            return false
        }
        if(dataBinding.password.text.length <6){
            Toast.makeText(applicationContext,"密码不得小于6位！",Toast.LENGTH_SHORT).show()
            return false
        }
        if(dataBinding.verifyCode.text.length != 4){
            Toast.makeText(applicationContext,"验证码必须为4位",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.get_verify_code -> {
                registerVM.getVerifyCode()
            }
            R.id.register_button -> {
                if(checkData())
                    registerVM.register(
                        dataBinding.email.text.toString(),
                        dataBinding.password.text.toString(),
                        dataBinding.verifyCode.text.toString()
                    )
            }
        }
    }
}