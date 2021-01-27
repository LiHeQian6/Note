package com.example.note_android.fragment.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R

class FollowFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followViewModel =
            ViewModelProvider(this).get(FollowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_follow, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        followViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}