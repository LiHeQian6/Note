package com.example.note_android.about.fragment.my_like_up

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note_android.R

class MyClickUpFragment : Fragment() {

    companion object {
        fun newInstance() = MyClickUpFragment()
    }

    private lateinit var viewModel: MyClickUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_click_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyClickUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}