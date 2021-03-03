package com.example.note_android.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.note_android.R
import kotlinx.android.synthetic.main.fragment_reset_pass.view.*

class ResetFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_reset_pass,container,false)
        initListener()
        return rootView
    }

    private fun changePassword(){

    }

    private fun checkData(): Boolean{
        var newPassword = rootView.new_password.text
        var confirmPassword = rootView.confirm_password.text
        if(newPassword.length < 6 || confirmPassword.length < 6){
            Toast.makeText(requireContext(),"密码长度小于6位", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!newPassword.equals(confirmPassword)){
            Toast.makeText(requireContext(),"两次输入密码不一致", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initListener() {
        rootView.reset_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reset_button -> {
                if(checkData()){
                    changePassword()
                }
            }
        }
    }
}