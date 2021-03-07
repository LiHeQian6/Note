package com.example.note_android.about.fragment.my_favourite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note_android.R

class MyLikeFragment : Fragment() {

    companion object {
        fun newInstance() = MyLikeFragment()
    }

    private lateinit var viewModel: MyLikeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_like_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyLikeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}