package com.example.note_android.login.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.note_android.R
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_forget_password.view.*
import kotlinx.android.synthetic.main.fragment_forget_password.view.verify_code

class ForgetFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_forget_password,container,false)
        rootView.forget_button.setOnClickListener(this)
        initListener()
        return rootView
    }

    private fun initListener() {
        rootView.get_forget_verify_code.setOnClickListener(this)
    }

    private fun commit(){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.forget_fragment,ResetFragment())
            .commit()
    }

    private fun getVerifyCode(){
        Toast.makeText(requireContext(),"获取失败", Toast.LENGTH_SHORT).show()
    }

    private fun checkData(): Boolean{
        if(!Patterns.EMAIL_ADDRESS.matcher(rootView.forget_email.text).matches()){
            Toast.makeText(requireContext(),"邮箱格式不合法！", Toast.LENGTH_SHORT).show()
            return false
        }
        if(rootView.verify_code.text.length != 4){
            Toast.makeText(requireContext(),"验证码必须为4位", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.forget_button -> {
                if(checkData()){
                    commit()
                }
            }
            R.id.get_forget_verify_code -> {
                getVerifyCode()
            }
        }
    }
}