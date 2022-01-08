package com.example.lecturer.view.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Question
import com.example.databasework.databinding.FragmentQuizzesBinding


import com.example.lecturer.view.adapter.QuizzesAdapter
import com.example.lecturer.view.interfaces.QuizzesInterface
import com.example.lecturer.view.viewmodel.QuizzesViewModel
import com.google.firebase.auth.FirebaseAuth


class QuizzesListFragment : Fragment(R.layout.fragment_quizzes) {
    private lateinit var binding: FragmentQuizzesBinding
    private lateinit var viewModel: QuizzesViewModel
    private lateinit var adapter: QuizzesAdapter
    private  val TAG = "QuizzesListFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quizzes, container, false)
        viewModel = ViewModelProvider(this)[QuizzesViewModel::class.java]

        setUpUIWithQuizzes()
        setupQuizzesListRecyclerView()




        return binding.root
    }

    private fun setUpUIWithQuizzes() {
        val pref =
            activity?.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        if (pref != null) {
            val year = pref.getString(getString(R.string.year), "")
            val subjectName = pref.getString(getString(R.string.subject_name), "")

            viewModel.getQuizzes(year!!, subjectName!!).observe(this) {
                adapter.submitList(it)

            }
        }

        Glide.with(requireContext())
            .load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
            .into(binding.quizzesUserImage)
    }

    private fun setupQuizzesListRecyclerView() {
        adapter = QuizzesAdapter(requireContext())
        binding.rvQuizzes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuizzes.adapter = adapter
        adapter.setOnClickListener(object : QuizzesInterface {
            override fun onQuizEnterClicked(
                questions: ArrayList<Question>,
                quizName: String,
                quizTime: Long
            ) {
                Log.d(TAG, "onQuizEnterClicked: $questions")
                findNavController().navigate(
                    QuizzesListFragmentDirections.actionQuizzesFragmentToQuizFragment(
                        questions.toTypedArray(),
                        quizName,
                        quizTime
                        )
                )
            }
        })
    }


}