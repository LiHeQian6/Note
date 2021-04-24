package com.example.note_android.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.note_android.listener.HttpListener
import com.example.note_android.R
import com.example.note_android.login.registerModel.RegisterVM
import com.example.note_android.util.SystemCode
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var registerVM: RegisterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerVM = RegisterVM(this)
        initView()
        initListener()
    }

    private fun initView() {
    }

    private fun initListener() {
        get_verify_code.setOnClickListener(this)
        register_button.setOnClickListener(this)
        registerVM.setHttpListener(object :
            HttpListener {
            override fun complete(dataType: String, data: String) {
                if(data == "Success"){
                    intent.putExtra("result","Success")
                    intent.putExtra("email",email.text)
                    intent.putExtra("password",password.text)
                    setResult(SystemCode.REGISTER,intent)
                    finish()
                }else{
                    intent.putExtra("result","Fail")
                    setResult(SystemCode.REGISTER,intent)
                }
            }
        })
    }

    private fun checkData(option:Int):Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            Toast.makeText(applicationContext,"邮箱格式不合法！",Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.text.length <6){
            Toast.makeText(applicationContext,"密码不得小于6位！",Toast.LENGTH_SHORT).show()
            return false
        }
        if(option == 1 && verify_code.text.length != 4){
            Toast.makeText(applicationContext,"验证码必须为4位",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.get_verify_code -> {
                if(checkData(0)) {
                    registerVM.getVerifyCode(email.text.toString())
                }
            }
            R.id.register_button -> {
                if(checkData(1))
                    registerVM.register(
                        email.text.toString(),
                        password.text.toString(),
                        verify_code.text.toString()
                    )
            }
        }
    }

    override fun onDestroy() {
        intent.putExtra("result","Fail")
        setResult(SystemCode.REGISTER,intent)
        super.onDestroy()
    }
}