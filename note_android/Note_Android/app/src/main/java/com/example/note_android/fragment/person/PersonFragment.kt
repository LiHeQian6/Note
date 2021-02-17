package com.example.note_android.fragment.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.annotation.Page
import com.example.note_android.login.LoginActivity
import com.example.note_android.util.ActivityUtil
import kotlinx.android.synthetic.main.fragment_person.view.*

@Page(name = "个人信息页面")
class PersonFragment : Fragment(),View.OnClickListener {

    private lateinit var personViewModel: PersonViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_person, container, false)
        initView(root)
        return root
    }

    private fun initView(root: View) {
        root.logout_button.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout_button -> {
                ActivityUtil.get()?.goActivityKill(requireContext(),LoginActivity::class.java)
            }
        }
    }


}