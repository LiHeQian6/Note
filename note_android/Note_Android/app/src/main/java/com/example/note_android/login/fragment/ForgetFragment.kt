package com.example.note_android.login.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.Fragment
import com.example.note_android.R
import com.example.note_android.login.CountDownTimer
import com.example.note_android.util.HttpAddressUtil
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_forget_password.view.*
import kotlinx.android.synthetic.main.fragment_forget_password.view.verify_code
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ForgetFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var handler: Handler
    private lateinit var email: String
    private lateinit var verifyCode: String
    private lateinit var newPassword: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_forget_password,container,false)
        rootView.forget_button.setOnClickListener(this)
        initListener()
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                when(msg.what){
                    0 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS") {
                            Toast.makeText(requireContext(), "发送成功，请查收！", Toast.LENGTH_SHORT).show()
                            CountDownTimer(rootView.get_forget_verify_code).run()
                        }else
                            Toast.makeText(requireContext(),"发送失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        if(result.get("status") != null && result.get("status") == "SUCCESS") {
                            requireActivity().finish()
                        }else
                            Toast.makeText(requireContext(),"验证失败，请重试！",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return rootView
    }

    private fun initListener() {
        rootView.get_forget_verify_code.setOnClickListener(this)
        rootView.forget_button.setOnClickListener(this)
    }

    private fun commit(){
        val httpClient = OkHttpClient()
        val formBody = FormBody.Builder()
                .add("newPassword", newPassword)
                .add("username", email)
                .build()
        val urlBuilder = HttpAddressUtil.resetPassword().toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addPathSegment(verifyCode)
        val request = Request.Builder().url(urlBuilder?.build()!!).post(formBody).build()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(),"网络出了点问题",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(requireContext(),"数据出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 1
                handler.sendMessage(mess)
            }
        })
    }

    private fun getVerifyCode(email: String){
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getVerifyCodeIP().toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("mail",email)
        urlBuilder?.addPathSegment("修改密码")
        val request = Request.Builder().url(urlBuilder?.build()!!).get().build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(),"网络出了点问题",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if(result == null || result == ""){
                    Toast.makeText(requireContext(),"数据出了点问题,请重试",Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                mess.what = 0
                handler.sendMessage(mess)
            }
        })
    }

    private fun checkData(option: Int): Boolean{
        var newPassword = rootView.new_password.text.toString()
        var confirmPassword = rootView.confirm_password.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(rootView.forget_email.text).matches()){
            Toast.makeText(requireContext(),"邮箱格式不合法！", Toast.LENGTH_SHORT).show()
            return false
        }
        if(newPassword != confirmPassword){
            Toast.makeText(requireContext(),"两次输入密码不一致！", Toast.LENGTH_SHORT).show()
            return false
        }
        if(option == 1 && rootView.verify_code.text.length != 4){
            Toast.makeText(requireContext(),"验证码必须为4位", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.forget_button -> {
                if(checkData(1)){
                    email = rootView.forget_email.text.toString()
                    verifyCode = rootView.verify_code.text.toString()
                    newPassword = rootView.new_password.text.toString()
                    commit()
                }
            }
            R.id.get_forget_verify_code -> {
                if(checkData(0)) {
                    getVerifyCode(rootView.forget_email.text.toString())
                }
            }
        }
    }
}