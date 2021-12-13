package com.example.lecturer.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.databasework.R


class QuizzesFragment : Fragment(R.layout.fragment_quizzes) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!(activity as AppCompatActivity).supportActionBar!!.isShowing){
            (activity as AppCompatActivity).supportActionBar!!.show()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}